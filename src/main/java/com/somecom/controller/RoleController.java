package com.somecom.controller;

import com.somecom.data.PageSort;
import com.somecom.entity.FavoriteRole;
import com.somecom.entity.PayOrder;
import com.somecom.entity.Role;
import com.somecom.entity.RoleApply;
import com.somecom.entity.User;
import com.somecom.enums.BusinessType;
import com.somecom.enums.PayOrderStatusEnum;
import com.somecom.model.ResultVo;
import com.somecom.repo.FavoriteRoleRepository;
import com.somecom.repo.PayOrderRepository;
import com.somecom.repo.RoleApplyRepository;
import com.somecom.repo.RoleRepository;
import com.somecom.repo.UserRepository;
import com.somecom.service.PaymentService;
import com.somecom.util.PayUtil;
import com.somecom.util.WechatPayParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.somecom.consts.PaymentConst.HANDLE_RATE;
import static com.somecom.consts.PaymentConst.MIN_CHARGE;

@RestController
@Api(tags = "角色管理")
@RequestMapping(path = "/role")
public class RoleController {
    @Value("${wx.file.path}")
    private String filePath;

    @Autowired
    private RoleRepository roleRepository;
    @Resource
    private FavoriteRoleRepository favoriteRoleRepository;
    @Autowired
    private RoleApplyRepository roleApplyRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private PaymentService paymentService;
    @Resource
    private PayOrderRepository payOrderRepository;

    @ApiOperation(value = "查单个角色", notes = "查单个角色")
    @GetMapping("/{roleId}")
    public ResultVo single(@PathVariable("roleId") Integer roleId) {
        Optional<Role> byId = roleRepository.findById(roleId);
        byId.orElseThrow(() -> new IllegalArgumentException("No role of id [" + roleId + "] found."));
        return ResultVo.ok(byId.get());
    }

    @ApiOperation(value = "完成角色征用并支付", notes = "完成角色征用并支付")
    @GetMapping("/{roleId}/finishedBy/{userId}")
    public ResultVo finishedBy(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        Optional<Role> byId = roleRepository.findById(roleId);
        byId.ifPresent(role -> {
            if (role.getAcceptedById().equals(userId)) {
                role.setStatus("已完成");
                roleRepository.saveAndFlush(role);
                //todo 发起平台到人力的支付或者记账后周结日打款
            }
        });
        byId.orElseThrow(() -> new IllegalArgumentException("No role of id [" + roleId + "] found."));
        return ResultVo.ok();
    }

    @ApiOperation(value = "接受被征用", notes = "接受被征用")
    @GetMapping("/{roleId}/acceptBy/{userId}")
    public ResultVo acceptBy(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        Optional<Role> byId = roleRepository.findById(roleId);
        try {
            byId.ifPresent(role -> {
                if (Objects.isNull(role.getAcceptedById())) {
                    role.setAcceptedById(userId);
                    role.setStatus("进行中");
                    roleRepository.saveAndFlush(role);
                } else {
                    throw new RuntimeException("已经被其它人接单");
                }
            });
        } catch (RuntimeException e) {
            return ResultVo.err(e.getMessage());
        }
        byId.orElseThrow(() -> new IllegalArgumentException("No role of id [" + roleId + "] found."));
        return ResultVo.ok();
    }

    @ApiOperation(value = "申请角色", notes = "申请角色并付款")
    @GetMapping("/{roleId}/applyBy/{userId}")
    @Transactional
    public ResultVo apply(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        Optional<Role> byId = roleRepository.findById(roleId);
        final ResultVo[] result = {new ResultVo()};
        byId.ifPresent(role -> {
            Set<Integer> set = new HashSet<>();
            if (Objects.nonNull(role.getApplyPersons())) {
                result[0].setRet(600);
                result[0].setMsg("抱歉，角er已被征用，请选择其它角er或者等待该角er空闲");
                result[0].setData("已被" + role.getApplyPersons() + "征用");
            } else {
                set.add(userId);
                role.setStatus("待确认");
                role.setApplyPersons(StringUtils.collectionToCommaDelimitedString(set));
                roleRepository.saveAndFlush(role);

                RoleApply roleApply = new RoleApply();
                roleApply.setRole(role);
                roleApply.setUserId(userId);
                List<RoleApply> all = roleApplyRepository.findAll(Example.of(roleApply));
                if (CollectionUtils.isEmpty(all)) {
                    roleApplyRepository.save(roleApply);
                }

                //支付
                Optional<User> byId1 = userRepository.findById(userId);
                byId1.orElseThrow(() -> new IllegalArgumentException("当前操作用户不存在"));

                PayOrder payOrder = new PayOrder();
                payOrder.setBody("角er-角色征用");
                payOrder.setBusinessId(roleId);
                payOrder.setBusinessType(BusinessType.ROLE.getCode());
                payOrder.setCreateBy(userId);
                //计算手本次支付应付续费
                BigDecimal charge = role.getFee().multiply(HANDLE_RATE).max(MIN_CHARGE);
                payOrder.setFee(role.getFee().add(charge));
                payOrder.setHandleRate(HANDLE_RATE);
                payOrder.setMinCharge(MIN_CHARGE);
                payOrder.setStatus(PayOrderStatusEnum.NEW.getCode());

                WechatPayParam wechatPayParam = new WechatPayParam();
                wechatPayParam.setBody(payOrder.getBody());
                wechatPayParam.setFee(payOrder.getFee());
                wechatPayParam.setOpenId(byId1.get().getOpenid());
                String s = System.currentTimeMillis() + UUID.randomUUID().toString();
                String outTradeNo = PayUtil.md5(s);
                wechatPayParam.setOrderId(outTradeNo);
                payOrder.setOutTradeNo(outTradeNo);
                result[0] = paymentService.pay(wechatPayParam);
                if (result[0].getRet() != 200) {
                    throw new RuntimeException("统一下单失败，事务回滚，详情：" + result[0].getMsg());
                }
                payOrder.setStatus(PayOrderStatusEnum.WAIT_FOR_PAY.getCode());
                payOrderRepository.saveAndFlush(payOrder);
            }
        });
        byId.orElseThrow(() -> new IllegalArgumentException("No role of id [" + roleId + "] found."));
        return result[0];
    }

    @ApiOperation(value = "查询角色列表", notes = "默认查询全部，无分页")
    @GetMapping(path = "/list/{userId}/{index}")
    public ResultVo getRoleList(Role role, @PathVariable("userId") Integer userId, @PathVariable("index") Integer index) {
//        List<Role> roles;
//        if (!StringUtils.hasText(role.getName()) && !StringUtils.hasText(role.getSkill())) {
//            roles = roleRepository.findAll(Sort.by("id").descending());
//        } else {
//            roles = roleRepository.findByNameContainsAndSkillContainsOrderByIdDesc(role.getName(), role.getSkill());
//        }
        ////
// 创建分页对象
        PageRequest page = PageSort.of(index);

        // 使用Specification复杂查询
        Page<Role> roles = roleRepository.findAll((Specification<Role>) (root, query, cb) -> {
            List<Predicate> preList = new ArrayList<>();
            if (role.getId() != null) {
                preList.add(cb.equal(root.get("id").as(Integer.class), role.getId()));
            }
            if (StringUtils.hasText(role.getName())) {
                preList.add(cb.like(root.get("name").as(String.class), "%" + role.getName() + "%"));
            }
            if (StringUtils.hasText(role.getSkill())) {
                preList.add(cb.like(root.get("skill").as(String.class), "%" + role.getSkill() + "%"));
            }

            Predicate[] pres = new Predicate[preList.size()];
            return query.where(preList.toArray(pres)).getRestriction();
        }, page);
        ////
        if (roles.isEmpty()) {
            return ResultVo.ok();
        }
        if (userId != -1) {
            Stream<FavoriteRole> all = favoriteRoleRepository.findAll(Example.of(FavoriteRole.ofUser(userId))).stream();
            List<Role> collect = all.map(FavoriteRole::getRole).sorted(Comparator.comparing(Role::getId)).collect(Collectors.toList());
            roles.forEach(originRole -> {
                if (collect.contains(originRole)) {
                    originRole.setCollection(true);
                } else {
                    originRole.setCollection(false);
                }
            });
        }
        return ResultVo.ok(roles.getContent());
    }

    @ApiOperation(value = "新增角色", notes = "前端做字段完整校验")
    @PostMapping("/add")
    @Transactional
    public ResultVo addRole(@RequestBody Role role, Integer userId) {
        Optional<User> byId = userRepository.findById(userId);
        AtomicReference<Role> save = new AtomicReference<>();
        byId.ifPresent(
                user -> {
                    if (Objects.nonNull(role.getId())) {
                        Optional<Role> byId1 = roleRepository.findById(role.getId());
                        byId1.ifPresent(role1 -> {
                            BeanUtils.copyProperties(role, role1);
                            roleRepository.saveAndFlush(role1);
                        });
                    } else {
                        role.setStatus("已发布");
                        String urls = role.getImgUrl();
                        String[] imgs;
                        if (Objects.nonNull(imgs = StringUtils.split(urls, ","))) {
                            role.setImg(imgs[0]);
                        }
                        save.set(roleRepository.save(role));
                        user.setTalentId(role.getId());
                        userRepository.saveAndFlush(user);
                    }
                }
        );
        byId.orElseThrow(() -> new IllegalArgumentException("No user of id [" + userId + "] found."));
        return ResultVo.ok(save.get());
    }

    @ApiOperation(value = "删除角色", notes = "根据ID，根据属性都可以删除对应角色")
    @GetMapping("/delete")
    public ResultVo delRole(Role role) {
        roleRepository.delete(role);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新角色", notes = "后台无校验字段合法性")
    @PostMapping("update")
    public ResultVo updateRole(@RequestBody Role role) {
        return ResultVo.ok(roleRepository.saveAndFlush(role));
    }
}