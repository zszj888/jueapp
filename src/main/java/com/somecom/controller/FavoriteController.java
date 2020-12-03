package com.somecom.controller;

import com.somecom.entity.FavoriteRole;
import com.somecom.entity.FavoriteTask;
import com.somecom.entity.Role;
import com.somecom.entity.Task;
import com.somecom.model.ResultVo;
import com.somecom.repo.FavoriteRoleRepository;
import com.somecom.repo.FavoriteTaskRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户收藏的任务")
@RequestMapping(path = "/favor")
public class FavoriteController {
    @Resource
    private FavoriteRoleRepository favoriteRoleRepository;
    @Resource
    private FavoriteTaskRepository favoriteTaskRepository;

    @ApiOperation(value = "用户取消收藏", notes = "传入用户ID，和roleId")
    @DeleteMapping(path = "/{userId}/{roleId}")
    public ResultVo deleteFavorRole(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        favoriteRoleRepository.deleteByUserIdAndRoleIs(userId, Role.of(roleId));
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户新增收藏", notes = "传入用户ID，和roleId")
    @PostMapping(path = "/{userId}/{roleId}")
    public ResultVo favorRole(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        FavoriteRole favoriteRole = new FavoriteRole();
        favoriteRole.setClassifyName("收藏1");
        favoriteRole.setUserId(userId);
        favoriteRole.setRole(Role.of(roleId));
        favoriteRoleRepository.save(favoriteRole);
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户取消收藏任务", notes = "传入用户ID，taskId")
    @DeleteMapping(path = "/{userId}/t/{taskId}")
    public ResultVo deleteFavorTask(@PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {
        favoriteTaskRepository.deleteByUserIdAndTaskIs(userId, Task.of(taskId));
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户收藏任务", notes = "传入用户ID，taskId")
    @PostMapping(path = "/{userId}/t/{taskId}")
    public ResultVo favorTask(@PathVariable("userId") Integer userId, @PathVariable("taskId") Integer taskId) {
        FavoriteTask favoriteTask = new FavoriteTask();
        favoriteTask.setClassifyName("收藏1");
        favoriteTask.setUserId(userId);
        favoriteTask.setTask(Task.of(taskId));
        favoriteTaskRepository.save(favoriteTask);
        return ResultVo.ok();
    }

    @ApiOperation(value = "用户收藏的任务", notes = "传入用户ID，查询收藏的任务列表")
    @GetMapping(path = "/{userId}/task")
    public ResultVo favorTask(@PathVariable("userId") Integer userId) {
        List<FavoriteTask> all = favoriteTaskRepository.findAll(Example.of(FavoriteTask.ofUser(userId)));
        List<Task> collect = all.stream().map(FavoriteTask::getTask).sorted(Comparator.comparing(Task::getId).reversed()).collect(Collectors.toList());
        return ResultVo.ok(collect);
    }

    @ApiOperation(value = "用户收藏的角色", notes = "传入用户ID，查询收藏的角色列表")
    @GetMapping(path = "/{userId}/role")
    public ResultVo favorRole(@PathVariable("userId") Integer userId) {
        List<FavoriteRole> all = favoriteRoleRepository.findAll(Example.of(FavoriteRole.ofUser(userId)));
        List<Role> collect = all.stream().map(FavoriteRole::getRole).sorted(Comparator.comparing(Role::getId).reversed()).collect(Collectors.toList());
        return ResultVo.ok(collect);
    }

}