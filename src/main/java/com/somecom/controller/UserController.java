package com.somecom.controller;

import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(tags = "用户管理")
@RequestMapping(path = "/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "查询用户列表", notes = "暂时没有分页，直接返回库里全部")
    @GetMapping(path = "/getUserList")
    public ResultVo getUserList(User example) {
        return ResultVo.ok(userRepository.findAll(Example.of(example)));
    }

    @ApiOperation(value = "新增用户", notes = "注意确保字段的合法性")
    @PostMapping("/addUserInfo")
    public ResultVo addUser(User user) {
        user.setCreateTime(new Date());
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
    public ResultVo updateUser(User user) {
        return ResultVo.ok(userRepository.saveAndFlush(user));
    }
}