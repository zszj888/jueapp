package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleRepository;
import com.somecom.repo.UserRepository;
import com.somecom.util.SystemUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "角色管理")
@RequestMapping(path = "/api/v1")
public class RoleController {
    @Value("${wx.file.path}")
    private String filePath;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "查询角色列表", notes = "默认查询全部，无分页")
    @GetMapping(path = "/getRoleList")
    public ResultVo getRoleList(Role role) {
        List<Role> roles = roleRepository.findAll(Example.of(role), Sort.by("createTime").descending());
        return ResultVo.ok(roles);
    }

    @ApiOperation(value = "新增角色", notes = "前端做字段完整校验")
    @PostMapping("/addRoleInfo")
    @Transactional
    public ResultVo addRole(@RequestBody Role role) {
        role.setCreateTime(LocalDateTime.now());
        Role save = roleRepository.save(role);
        Optional<User> one = userRepository.findOne(Example.of(new User(role.getOpenid())));
        if (!one.isPresent())
            return ResultVo.err("openid " + role.getOpenid() + "没有user info");
        SystemUtil.login(one.get());
        return ResultVo.ok(save);
    }

    @ApiOperation(value = "删除角色", notes = "根据ID，根据属性都可以删除对应角色")
    @GetMapping("/deleteRole")
    public ResultVo delRole(Role role) {
        roleRepository.delete(role);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新角色", notes = "后台无校验字段合法性")
    @PostMapping("updateRole")
    public ResultVo updateRole(@RequestBody Role role) {
        return ResultVo.ok(roleRepository.saveAndFlush(role));
    }
}