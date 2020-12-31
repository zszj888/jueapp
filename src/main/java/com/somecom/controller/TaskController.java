package com.somecom.controller;

import com.somecom.data.PageSort;
import com.somecom.entity.FavoriteTask;
import com.somecom.entity.PayOrder;
import com.somecom.entity.Task;
import com.somecom.entity.TaskApply;
import com.somecom.entity.Transaction;
import com.somecom.entity.User;
import com.somecom.enums.BusinessType;
import com.somecom.enums.PayOrderStatusEnum;
import com.somecom.model.ResultVo;
import com.somecom.repo.FavoriteTaskRepository;
import com.somecom.repo.PayOrderRepository;
import com.somecom.repo.TaskApplyRepository;
import com.somecom.repo.TaskRepository;
import com.somecom.repo.TransactionRepository;
import com.somecom.repo.UserRepository;
import com.somecom.service.PaymentService;
import com.somecom.util.PayUtil;
import com.somecom.util.WechatPayParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.somecom.consts.PaymentConst.HANDLE_RATE;
import static com.somecom.consts.PaymentConst.MIN_CHARGE;

@RestController
@Api(tags = "任务管理")
@RequestMapping(path = "/task")
public class TaskController {
    @Resource
    private TaskRepository taskRepository;
    @Resource
    private FavoriteTaskRepository favoriteTaskRepository;
    @Autowired
    private TaskApplyRepository taskApplyRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private PaymentService paymentService;
    @Resource
    private PayOrderRepository payOrderRepository;
    @Resource
    private TransactionRepository transactionRepository;

    @ApiOperation(value = "接受任务", notes = "接受任务")
    @GetMapping("/{taskId}/acceptBy/{userId}")
    @Transactional
    public ResultVo acceptBy(@PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {
        Optional<Task> byId = taskRepository.findById(taskId);
        final ResultVo[] result = {new ResultVo()};
        byId.ifPresent(task -> {
            Set<Integer> set = new HashSet<>();
            if (Objects.nonNull(task.getAcceptedById())) {
                result[0].setRet(600);
                result[0].setMsg("重复征用");
                result[0].setData("已征用" + task.getAcceptedById());
            } else {
                set.add(userId);
                task.setStatus("进行中");
                task.setApplyPersons(StringUtils.collectionToCommaDelimitedString(set));
                taskRepository.saveAndFlush(task);

                //支付
                Optional<User> byId1 = userRepository.findById(task.getCreateById());
                byId1.orElseThrow(() -> new IllegalArgumentException("当前操作用户不存在"));

                PayOrder payOrder = new PayOrder();
                payOrder.setBody("角er-任务征用");
                payOrder.setBusinessId(taskId);
                payOrder.setBusinessType(BusinessType.TASK.getCode());
                payOrder.setCreateBy(userId);
                //本次手续费
                BigDecimal charge = task.getFee().multiply(HANDLE_RATE).max(MIN_CHARGE);
                payOrder.setFee(task.getFee().add(charge));
                payOrder.setHandleRate(HANDLE_RATE);
                payOrder.setMinCharge(MIN_CHARGE);
                payOrder.setStatus(PayOrderStatusEnum.NEW.getCode());
                String s = System.currentTimeMillis() + UUID.randomUUID().toString();
                String outTradeNo = PayUtil.md5(s);
                payOrder.setOutTradeNo(outTradeNo);

                WechatPayParam wechatPayParam = new WechatPayParam();
                wechatPayParam.setOrderId(outTradeNo);
                wechatPayParam.setBody(payOrder.getBody());
                wechatPayParam.setFee(payOrder.getFee());
                wechatPayParam.setOpenId(byId1.get().getOpenid());

                result[0] = paymentService.pay(wechatPayParam);
                if (result[0].getRet() != 200) {
                    throw new RuntimeException("统一下单失败，事务回滚，详情：" + result[0].getMsg());
                }
                payOrder.setStatus(PayOrderStatusEnum.WAIT_FOR_PAY.getCode());
                payOrderRepository.saveAndFlush(payOrder);
            }
        });
        byId.orElseThrow(() -> new IllegalArgumentException("No task of id [" + taskId + "] found."));
        return result[0];
    }

    @ApiOperation(value = "完成任务", notes = "完成任务")
    @GetMapping("/{taskId}/finishedBy/{userId}")
    @Transactional
    public ResultVo finishedBy(@PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {
        Optional<Task> byId = taskRepository.findById(taskId);
        byId.ifPresent(task -> {
            if (task.getCreateById().equals(userId)) {
                task.setStatus("已完成");
                taskRepository.saveAndFlush(task);
                Optional<User> byId1 = userRepository.findById(task.getAcceptedById());
                if (byId1.isPresent()) {
                    User user = byId1.get();
                    if (Objects.isNull(user.getBalance())) {
                        user.setBalance(BigDecimal.ZERO);
                    }
                    user.setBalance(user.getBalance().add(task.getFee()));
                    userRepository.saveAndFlush(user);
                    Transaction transaction = new Transaction();
                    transaction.setUserId(user.getId());
                    transaction.setAmount(task.getFee());
                    transaction.setTime(LocalDateTime.now());
                    transaction.setLastUpdateTime(LocalDateTime.now());
                    transaction.setVersion(0);
                    transactionRepository.saveAndFlush(transaction);
                }
                byId1.orElseThrow(() -> new IllegalArgumentException("task.AcceptedById of id [" + task.getAcceptedById() + "] not found."));
            }
        });
        byId.orElseThrow(() -> new IllegalArgumentException("No task of id [" + taskId + "] found."));
        return ResultVo.ok();
    }

    @ApiOperation(value = "申请任务", notes = "申请任务")
    @GetMapping("/{taskId}/applyBy/{userId}")
    @Transactional
    public ResultVo apply(@PathVariable("userId") String userId, @PathVariable("taskId") Integer taskId) {
        Optional<Task> byId = taskRepository.findById(taskId);
        byId.ifPresent(task -> {
            Set<String> set = new HashSet<>();
            if (Objects.nonNull(task.getApplyPersons())) {
                set.addAll(Arrays.asList(task.getApplyPersons().split(",")));
            }
            set.add(userId);
            task.setStatus("待确认");
            task.setApplyPersons(StringUtils.collectionToCommaDelimitedString(set));
            taskRepository.saveAndFlush(task);

            TaskApply taskApply = new TaskApply();
            taskApply.setTask(task);
            taskApply.setUserId(Integer.valueOf(userId));
            List<TaskApply> all = taskApplyRepository.findAll(Example.of(taskApply));
            if (CollectionUtils.isEmpty(all)) {
                taskApplyRepository.save(taskApply);
            }
        });
        byId.orElseThrow(() -> new IllegalArgumentException("No task of id [" + taskId + "] found."));
        return ResultVo.ok();
    }

    @ApiOperation(value = "查询任务列表", notes = "默认查询全部，有分页")
    @GetMapping(path = "/list/{userId}/{index}")
    public ResultVo getTaskList(Task example, @PathVariable("userId") Integer userId, @PathVariable("index") Integer index) {
        if (StringUtils.hasText(example.getPosition()) && !StringUtils.hasText(example.getSkill()) ) {
            example.setSkill(example.getPosition());
        }
        if (StringUtils.hasText(example.getSkill()) && !StringUtils.hasText(example.getPosition()) ) {
            example.setPosition(example.getSkill());
        }
        Page<Task> tasks = taskRepository.findAll((Specification<Task>) (root, query, criteriaBuilder) -> {
            Predicate position = criteriaBuilder.like(root.get("position"), "%" + example.getPosition() + "%");
            Predicate skill = criteriaBuilder.like(root.get("skill"), "%" + example.getSkill() + "%");
            return query.where(criteriaBuilder.or(position, skill)).getRestriction();
        }, PageSort.of(index));
        if (CollectionUtils.isEmpty(tasks.getContent())) {
            return ResultVo.ok();
        }
        if (userId != -1) {
            Stream<FavoriteTask> all = favoriteTaskRepository.findAll(Example.of(FavoriteTask.ofUser(userId))).stream();
            List<Task> collect = all.map(FavoriteTask::getTask).collect(Collectors.toList());
            tasks.forEach(originTask -> {
                if (collect.contains(originTask)) {
                    originTask.setCollection(true);
                } else {
                    originTask.setCollection(false);
                }
            });
        }
        tasks.getContent().forEach(task -> task.setValidate(task.getTaskTime().isBefore(LocalDateTime.now())));
        return ResultVo.ok(tasks.getContent());
    }

    @ApiOperation(value = "新增任务", notes = "确保字段合法")
    @PostMapping("/add")
    public ResultVo addTask(@RequestBody Task task) {
        List<Task> one = taskRepository.findAll(Example.of(Task.ofNameAndUserId(task.getName(), task.getCreateById())));
        if (!one.isEmpty()) {
            return ResultVo.err("任务名重复了");
        }
        task.setStatus("已发布");
        return ResultVo.ok(taskRepository.save(task));
    }

    @ApiOperation(value = "删除任务", notes = "传入合法字段即可删除对应的任务")
    @GetMapping("/delete")
    public ResultVo delTask(Task role) {
        taskRepository.delete(role);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新任务", notes = "传入合法字段即可")
    @PostMapping("update")
    public ResultVo updateTask(@RequestBody Task role) {
        return ResultVo.ok(taskRepository.saveAndFlush(role));
    }
}