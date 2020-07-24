package com.somecom.controller;

import com.somecom.entity.Task;
import com.somecom.model.ResultVo;
import com.somecom.repo.TaskRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "任务管理")
@RequestMapping(path = "/api/v1")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @ApiOperation(value = "查询任务列表", notes = "默认查询全部，无分页")
    @GetMapping(path = "/getTaskList")
    public ResultVo getTaskList() {
        return ResultVo.ok(taskRepository.findAll());
    }

    @ApiOperation(value = "新增任务", notes = "确保字段合法")
    @PostMapping("/addTask")
    public ResultVo addTask(Task role) {
        return ResultVo.ok(taskRepository.save(role));
    }

    @ApiOperation(value = "删除任务", notes = "传入合法字段即可删除对应的任务")
    @GetMapping("/deleteTask")
    public ResultVo delTask(Task role) {
        taskRepository.delete(role);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新任务", notes = "传入合法字段即可")
    @PostMapping("updateTask")
    public ResultVo updateTask(Task role) {
        return ResultVo.ok(taskRepository.saveAndFlush(role));
    }
}