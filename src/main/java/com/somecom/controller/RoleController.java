package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@RequestMapping(path = "/api/v1")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(path = "/getRoleList")
    public ResultVo getRoleList() {
        return ResultVo.ok(roleRepository.findAll());
    }

    @PostMapping("/addRoleInfo")
    public ResultVo addRole(Role role) {
        return ResultVo.ok(roleRepository.save(role));
    }

    @GetMapping("/deleteRole")
    public ResultVo delRole(Role role) {
        roleRepository.delete(role);
        return ResultVo.ok();
    }

    @PostMapping("updateRole")
    public ResultVo updateRole(Role role) {
        return ResultVo.ok(roleRepository.saveAndFlush(role));
    }
}