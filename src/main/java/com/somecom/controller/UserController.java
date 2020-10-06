package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleRepository;
import com.somecom.repo.TaskRepository;
import com.somecom.repo.UserRepository;
import com.somecom.util.SystemUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RestController
@Api(tags = "用户管理")
@RequestMapping(path = "/api/v1")
public class UserController {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private TaskRepository taskRepository;

    @ApiOperation(value = "收藏角色", notes = "角色大厅，收藏角色")
    @GetMapping(path = "/favorRole")
    @ApiImplicitParam(name = "roleId", required = true, dataType = "int", paramType = "query")
    public ResultVo favorRole(Integer roleId) {
        SystemUtil.currentUser().ifPresent(user -> {
            Optional<Role> byId = roleRepository.findById(roleId);
            byId.ifPresent(role -> {
                user.setFavorRoles(Collections.singleton(role));
                userRepository.save(user);
            });
            byId.orElseThrow(() -> new RuntimeException("角色" + roleId + "不存在"));
        });
        SystemUtil.currentUser().orElseThrow(() -> new RuntimeException("未登录"));
        return ResultVo.ok();
    }

    @ApiOperation(value = "收藏任务", notes = "任务大厅，收藏任务")
    @GetMapping(path = "/favorTask")
    public ResultVo favorTask(Integer taskId) {
        SystemUtil.currentUser().ifPresent(user -> {
            user.setFavorTasks(Collections.singleton(taskRepository.findById(taskId).get()));
            userRepository.save(user);
        });
        SystemUtil.currentUser().orElseThrow(() -> new RuntimeException("未登录"));
        return ResultVo.ok();
    }

    @ApiOperation(value = "根据OPENID查用户个人资料", notes = "传入openid，返回该用户的个人资料")
    @GetMapping(path = "{openid}")
    public ResultVo openid(@PathVariable(name = "openid") String openid) {
        return ResultVo.ok(userRepository.findOne(Example.of(new User(openid))));
    }

    @ApiOperation(value = "查询用户列表", notes = "暂时没有分页，直接返回库里全部")
    @GetMapping(path = "/getUserList")
    public ResultVo getUserList(User example) {
        return ResultVo.ok(userRepository.findAll(Example.of(example), Sort.by("createTime").descending()));
    }

    @ApiOperation(value = "新增保存用户信息", notes = "注意确保字段的合法性")
    @PostMapping("/addUserInfo")
    public ResultVo addUser(@RequestBody User user) {
        user.setCreateTime(LocalDateTime.now());
        return ResultVo.ok(userRepository.save(user));
    }

    @ApiOperation(value = "删除用户", notes = "注意确保字段的合法性")
    @GetMapping("/deleteUser")
    public ResultVo delUser(User user) {
        userRepository.delete(user);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新用户", notes = "注意确保字段的合法性")
    @PostMapping("updateUser")
    public ResultVo updateUser(@RequestBody @Valid User user) {
        Optional<User> one = userRepository.findById(user.getId());
        one.orElseThrow(IllegalAccessError::new);
        one.ifPresent(o -> BeanUtils.copyProperties(user, o));
        return ResultVo.ok(userRepository.save(one.get()));
    }
}