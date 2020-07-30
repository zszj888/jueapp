package com.somecom.controller;

import com.somecom.entity.Role;
import com.somecom.model.ResultVo;
import com.somecom.repo.RoleRepository;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "角色管理")
@RequestMapping(path = "/api/v1")
public class RoleController {
    @Value("${wx.file.path}")
    private String filePath;

    @Autowired
    private RoleRepository roleRepository;

    @ApiOperation(value = "查询角色列表", notes = "默认查询全部，无分页")
    @GetMapping(path = "/getRoleList")
    public ResultVo getRoleList(Role role) {
        List<Role> roles = roleRepository.findAll(Example.of(role), Sort.by("createTime").descending());
        roles.forEach(rol -> {
            try {
                rol.setImg(Base64.getEncoder()
                        .encodeToString(Files.readAllBytes(Paths
                                .get(filePath, rol.getOpenid(), String.valueOf(rol.getId()), rol.getImg()))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return ResultVo.ok(roles);
    }

    @ApiOperation(value = "新增角色", notes = "前端做字段完整校验")
    @PostMapping("/addRoleInfo")
    @Transactional
    public ResultVo addRole(@RequestBody Role role) throws IOException {
        role.setCreateTime(new Date());
        Role save = roleRepository.save(role);
        Path newPath = Paths.get(filePath, save.getOpenid(), String.valueOf(save.getId()));
        if (!Files.exists(newPath)) Files.createDirectories(newPath);
        Files.move(Paths.get(filePath, save.getOpenid(), save.getImg()),
                newPath.resolve(save.getImg()));
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