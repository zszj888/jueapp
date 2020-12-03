package com.somecom.web;

import com.somecom.annotation.EntityParam;
import com.somecom.consts.AdminConst;
import com.somecom.entity.Menu;
import com.somecom.entity.SysRole;
import com.somecom.enums.ResultEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.exception.ResultException;
import com.somecom.service.SysMenuService;
import com.somecom.service.SysRoleService;
import com.somecom.shiro.ShiroUtil;
import com.somecom.utils.EntityBeanUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.StatusUtil;
import com.somecom.validator.RoleValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, SysRole role) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取角色列表
        Example<SysRole> example = Example.of(role, matcher);
        Page<SysRole> list = sysRoleService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "system/role/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return "system/role/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") SysRole role, Model model) {
        model.addAttribute("role", role);
        return "system/role/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param role  实体对象
     */
    @PostMapping("/save")
    @ResponseBody
    public SysResultVo save(@Validated RoleValid valid, @EntityParam SysRole role) {
        // 不允许操作管理员角色数据
        if (role.getId() != null && role.getId().equals(AdminConst.ADMIN_ROLE_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_AUTH);
        }

        // 判断角色编号是否重复
        if (sysRoleService.repeatByName(role)) {
            throw new ResultException(ResultEnum.ROLE_EXIST);
        }

        // 复制保留无需修改的数据
        if (role.getId() != null) {
            SysRole beRole = sysRoleService.getById(role.getId());
            String[] fields = {"users", "menus"};
            EntityBeanUtil.copyProperties(beRole, role, fields);
        }

        // 保存数据
        sysRoleService.save(role);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到授权页面
     */
    @GetMapping("/auth")
    public String toAuth(@RequestParam(value = "ids") Long id, Model model) {
        model.addAttribute("id", id);
        return "system/role/auth";
    }

    /**
     * 获取权限资源列表
     */
    @GetMapping("/authList")
    @ResponseBody
    public SysResultVo authList(@RequestParam(value = "ids") SysRole role) {
        // 获取指定角色权限资源
        Set<Menu> authMenus = role.getMenus();
        // 获取全部菜单列表
        List<Menu> list = menuService.getListBySortOk();
        // 融合两项数据
        list.forEach(menu -> {
            if (authMenus.contains(menu)) {
                menu.setRemark("auth:true");
            } else {
                menu.setRemark("");
            }
        });
        return ResultVoUtil.success(list);
    }

    /**
     * 保存授权信息
     */
    @PostMapping("/auth")
    @ResponseBody
    public SysResultVo auth(
            @RequestParam(value = "id", required = true) SysRole role,
            @RequestParam(value = "authId", required = false) HashSet<Menu> menus) {
        // 不允许操作管理员角色数据
        if (role.getId().equals(AdminConst.ADMIN_ROLE_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_AUTH);
        }

        // 更新角色菜单
        role.setMenus(menus);

        // 保存数据
        sysRoleService.save(role);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") SysRole role, Model model) {
        model.addAttribute("role", role);
        return "system/role/detail";
    }

    /**
     * 跳转到拥有该角色的用户列表页面
     */
    @GetMapping("/userList/{id}")
    public String toUserList(@PathVariable("id") SysRole role, Model model) {
        model.addAttribute("list", role.getUsers());
        return "system/role/userList";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @ResponseBody
    public SysResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 不能修改超级管理员角色状态
        if (ids.contains(AdminConst.ADMIN_ROLE_ID)) {
            throw new ResultException(ResultEnum.NO_ADMINROLE_STATUS);
        }

        // 更新状态
        SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
        if (sysRoleService.updateStatus(systemDataStatusEnum, ids)) {
            return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
