package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.entity.RoleApply;
import com.somecom.entity.SysUser;
import com.somecom.entity.Task;
import com.somecom.entity.TaskApply;
import com.somecom.entity.Transaction;
import com.somecom.entity.User;
import com.somecom.enums.TransactionType;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleApplyRepository;
import com.somecom.repo.TaskApplyRepository;
import com.somecom.repo.TransactionRepository;
import com.somecom.repo.UserRepository;
import com.somecom.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户管理")
@RequestMapping(path = "/user")
public class UserController {
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserService userService;
    @Resource
    private TaskApplyRepository taskApplyRepository;
    @Resource
    private RoleApplyRepository roleApplyRepository;
    @Resource
    private TransactionRepository transactionRepository;

    @ApiOperation(value = "提现", notes = "提现")
    @PostMapping("/withdraw")
    @Transactional
    public ResultVo withdraw(@RequestBody Transaction body) {
        if (Objects.nonNull(body.getUserId()) && Objects.nonNull(body.getAmount())) {
            Optional<User> byId = userRepository.findById(body.getUserId());
            byId.ifPresent(b -> {
                User user = byId.get();
                if (user.getBalance().compareTo(body.getAmount()) > 0) {
                    Transaction transaction = new Transaction();
                    transaction.setUserId(body.getUserId());
                    transaction.setAmount(body.getAmount());
                    transaction.setTime(LocalDateTime.now());
                    transaction.setLastUpdateTime(LocalDateTime.now());
                    transaction.setTransType(TransactionType.OUTCOME.getCode());
                    transactionRepository.saveAndFlush(transaction);
                    //todo 企业付款到微信余额
                } else {
                    throw new IllegalArgumentException("余额不足");
                }
            });
            byId.orElseThrow(() -> new IllegalArgumentException("用户未找到，id" + body.getUserId()));
            return ResultVo.ok();
        }
        return ResultVo.err("userId必传");

    }

    @ApiOperation(value = "查询余额明细", notes = "查询余额明细")
    @GetMapping(path = "/balanceDetail/{userId}")
    public ResultVo balanceDetail(@PathVariable("userId") Integer userId) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        return ResultVo.ok(transactionRepository.findAll(Example.of(transaction), Sort.by("time").descending()));
    }

    @ApiOperation(value = "根据OPENID查用户个人资料", notes = "传入openid，返回该用户的个人资料")
    @GetMapping(path = "openid/{openid}")
    public ResultVo openid(@PathVariable(name = "openid") String openid) {
        return ResultVo.ok(userRepository.findOne(Example.of(new User(openid))));
    }

    @ApiOperation(value = "查询用户列表", notes = "暂时没有分页，直接返回库里全部")
    @GetMapping(path = "/getUserList")
    public ResultVo getUserList(User example) {
        return ResultVo.ok(userRepository.findAll(Example.of(example), Sort.by("createTime").descending()));
    }

    @ApiOperation(value = "查单个用户", notes = "返回单个用户详情")
    @ApiImplicitParam(paramType = "query")
    @GetMapping(path = "/{id}")
    public ResultVo getOne(@PathVariable("id") Integer id) {
        Optional<User> byId = userRepository.findById(id);
        byId.orElseThrow(() -> new IllegalArgumentException("不存在的用户ID" + id));
        return ResultVo.ok(byId.get());
    }

    @ApiOperation(value = "新增保存用户信息", notes = "注意确保字段的合法性")
    @PostMapping("/updateUserMyInfo")
    public ResultVo updateUserMyInfo(@RequestBody User user) {
        if (Objects.nonNull(user.getId())) {
            Optional<User> byId = userRepository.findById(user.getId());
            byId.ifPresent(b -> {
                BeanUtils.copyProperties(user, b);
                b.setNick_name(user.getNick_name());
                b.setSex(user.getSex());
                b.setBirthda_day(user.getBirthda_day());
                b.setHeight(user.getHeight());
                b.setWeight(user.getWeight());
                b.setTalents(user.getTalents());
                userRepository.saveAndFlush(b);
                SysUser sysUser = userService.findByOpenId(b.getOpenid());
                SysUser newUser = b.toSysUser();
                sysUser.setNickname(newUser.getNickname());
                sysUser.setSex(newUser.getSex());
                userService.save(sysUser);
            });
            return ResultVo.ok();
        }
        return ResultVo.err("未登录");

    }

    @ApiOperation(value = "删除用户", notes = "注意确保字段的合法性")
    @GetMapping("/deleteUser")
    public ResultVo delUser(User user) {
        userRepository.delete(user);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新用户", notes = "注意确保字段的合法性")
    @PostMapping("updateRealNamePicture")
    @Transactional
    public ResultVo updateUserPicture(@RequestBody @Valid User user) {
        Optional<User> one = userRepository.findById(user.getId());
        one.orElseThrow(IllegalAccessError::new);
        one.ifPresent(o ->
                {
                    o.setPictureUrl(user.getPictureUrl());
                    o.setRealNameAuth(2);
                    SysUser sysUser = userService.findByOpenId(o.getOpenid());
                    sysUser.setPictureUrl(o.getPictureUrl());
                    sysUser.setRealNameAuth(2);
                    userService.save(sysUser);
                    userRepository.saveAndFlush(o);
                }
        );
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户的任务", notes = "用户的所有任务列表")
    @GetMapping("/{userId}/task")
    public ResultVo task(@PathVariable("userId") Integer userId, Integer type) {
        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new IllegalArgumentException("Bad user id" + userId));
        if (Objects.isNull(type)) {
            return ResultVo.ok(user.get().getTasks());
        }
        //已发布
        if (type == 0) {
            return ResultVo.ok(user.get().getTasks().stream().filter(task -> task.getStatus().equals("已发布")).collect(Collectors.toList()));
        }
        //待确认
        if (type == 1) {
            return ResultVo.ok(user.get().getTasks().stream().filter(task -> task.getStatus().equals("待确认")).collect(Collectors.toList()));
        }
        //进行中
        if (type == 2) {
            return ResultVo.ok(user.get().getTasks().stream().filter(task -> task.getStatus().equals("进行中")).sorted(Comparator.comparing(Task::getId).reversed()).collect(Collectors.toList()));
        }
        //已完成
        if (type == 3) {
            return ResultVo.ok(user.get().getTasks().stream().filter(task -> "已完成".equals(task.getStatus())).sorted(Comparator.comparing(Task::getId).reversed())
                    .collect(Collectors.toList()));
        }
        //已申请
        if (type == 4) {
            return ResultVo.ok(taskApplyRepository.findAll(Example.of(TaskApply.example(userId))).stream()
                    .map(TaskApply::getTask).peek(task -> task.setRemrks("申请")).sorted(Comparator.comparing(Task::getId).reversed()).collect(Collectors.toList()));
        }
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户的角色列表", notes = "用户的所有角色列表")
    @GetMapping("/{userId}/role")
    public ResultVo role(@PathVariable("userId") Integer userId, Integer type) {
        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new IllegalArgumentException("Bad user id" + userId));
        if (Objects.isNull(type)) {
            return ResultVo.ok(user.get().getRoles());
        }
        //已发布
        if (type == 0) {
            return ResultVo.ok(user.get().getRoles().stream().filter(r -> Objects.isNull(r.getApplyPersons()))
                    .sorted(Comparator.comparing(Role::getId).reversed()).collect(Collectors.toList()));
        }
        //待确认
        if (type == 1) {
            return ResultVo.ok(user.get().getRoles().stream().filter(role -> StringUtils.hasText(role.getApplyPersons())
                    && Objects.isNull(role.getAcceptedById())).sorted(Comparator.comparing(Role::getId).reversed()).collect(Collectors.toList()));
        }
        //进行中
        if (type == 2) {
            return ResultVo.ok(user.get().getRoles().stream().filter(role -> Objects.nonNull(role.getAcceptedById())
                    && !"已完成".equals(role.getStatus())).sorted(Comparator.comparing(Role::getId).reversed()).collect(Collectors.toList()));
        }
        //已完成
        if (type == 3) {
            return ResultVo.ok(user.get().getRoles().stream().filter(role -> "已完成".equals(role.getStatus()))
                    .collect(Collectors.toList()));
        }
        //已申请
        if (type == 4) {
            return ResultVo.ok(roleApplyRepository.findAll(Example.of(RoleApply.example(userId))).stream()
                    .map(RoleApply::getRole).peek(role -> role.setRemark("申请")).sorted(Comparator.comparing(Role::getId).reversed()).collect(Collectors.toList()));
        }
        return ResultVo.ok(user.get().getRoles());
    }
}