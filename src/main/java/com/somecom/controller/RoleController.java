package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(tags = "角色管理")
@RequestMapping(path = "/api/v1")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @ApiOperation(value = "查询角色列表", notes = "默认查询全部，无分页")
    @GetMapping(path = "/getRoleList")
    public ResultVo getRoleList() {
        return ResultVo.ok(roleRepository.findAll());
    }

    @ApiOperation(value = "新增角色", notes = "前端做字段完整校验")
    @PostMapping("/addRoleInfo")
    public ResultVo addRole(Role role) {
        role.setCreateTime(new Date());
        return ResultVo.ok(roleRepository.save(role));
    }

    @ApiOperation(value = "删除角色", notes = "根据ID，根据属性都可以删除对应角色")
    @GetMapping("/deleteRole")
    public ResultVo delRole(Role role) {
        roleRepository.delete(role);
        return ResultVo.ok();
    }

    @ApiOperation(value = "更新角色", notes = "后台无校验字段合法性")
    @PostMapping("updateRole")
    public ResultVo updateRole(Role role) {
        return ResultVo.ok(roleRepository.saveAndFlush(role));
    }
}