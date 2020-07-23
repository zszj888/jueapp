package com.somecom.controller;

import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/getUserList")
    public ResultVo getUserList() {
        return ResultVo.ok(userRepository.findAll());
    }

    @PostMapping("/addUserInfo")
    public ResultVo addUser(User user) {
        return ResultVo.ok(userRepository.save(user));
    }

    @GetMapping("/deleteUser")
    public ResultVo delUser(User user) {
        userRepository.delete(user);
        return ResultVo.ok();
    }

    @PostMapping("updateUser")
    public ResultVo updateUser(User user) {
        return ResultVo.ok(userRepository.saveAndFlush(user));
    }
}