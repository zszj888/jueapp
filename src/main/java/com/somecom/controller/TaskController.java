package com.somecom.controller;

import com.somecom.entity.Task;
import com.somecom.entity.Task;
import com.somecom.model.ResultVo;
import com.somecom.repo.TaskRepository;
import com.somecom.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(path = "/getTaskList")
    public ResultVo getTaskList() {
        return ResultVo.ok(taskRepository.findAll());
    }

    @PostMapping("/addTask")
    public ResultVo addTask(Task role) {
        return ResultVo.ok(taskRepository.save(role));
    }

    @GetMapping("/deleteTask")
    public ResultVo delTask(Task role) {
        taskRepository.delete(role);
        return ResultVo.ok();
    }

    @PostMapping("updateTask")
    public ResultVo updateTask(Task role) {
        return ResultVo.ok(taskRepository.saveAndFlush(role));
    }
}