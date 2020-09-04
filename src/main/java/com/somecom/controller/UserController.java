package com.somecom.controller;

import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Api(tags = "用户管理")
@RequestMapping(path = "/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

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