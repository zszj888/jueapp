package com.somecom.web;

import com.somecom.config.properties.UploadProjectProperties;
import com.somecom.consts.AdminConst;
import com.somecom.entity.SysRole;
import com.somecom.entity.SysUser;
import com.somecom.enums.ResultEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.exception.ResultException;
import com.somecom.service.UserService;
import com.somecom.shiro.ShiroUtil;
import com.somecom.utils.EncryptUtil;
import com.somecom.utils.EntityBeanUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.SpringContextUtil;
import com.somecom.utils.StatusUtil;
import com.somecom.validator.UserValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/user")
public class SysUserController {

    @Autowired
    private UserService userService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, SysUser user) {

        // 获取用户列表
        Page<SysUser> list = userService.getPageList(user);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "system/user/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return "system/user/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "system/user/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param user  实体对象
     */
    @PostMapping("/save")
    @ResponseBody
    public SysResultVo save(@Validated UserValid valid, SysUser user) {

        // 验证数据是否合格
        if (user.getId() == null) {

            // 判断密码是否为空
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = EncryptUtil.getRandomSalt();
            String encrypt = EncryptUtil.encrypt(user.getPassword(), salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        }

        // 判断用户名是否重复
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // 复制保留无需修改的数据
        if (user.getId() != null) {
            // 不允许操作超级管理员数据
            if (user.getId().equals(AdminConst.ADMIN_ID)
                /*  &&!ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)*/) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            SysUser beUser = userService.getById(user.getId());
            String[] fields = {"password", "salt", "picture", "sysRoles"};
            EntityBeanUtil.copyProperties(beUser, user, fields);
        }

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "system/user/detail";
    }

    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/pwd")
    public String toEditPassword(Model model, @RequestParam(value = "ids", required = false) List<Long> ids) {
        model.addAttribute("idList", ids);
        return "system/user/pwd";
    }

    /**
     * 修改密码
     */
    @PostMapping("/pwd")
    @ResponseBody
    public SysResultVo editPassword(String password, String confirm,
                                    @RequestParam(value = "ids", required = false) List<Long> ids,
                                    @RequestParam(value = "ids", required = false) List<SysUser> users) {

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }

        // 不允许操作超级管理员数据
        if (ids.contains(AdminConst.ADMIN_ID) /*&&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)*/) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 修改密码，对密码进行加密
        users.forEach(user -> {
            String salt = EncryptUtil.getRandomSalt();
            String encrypt = EncryptUtil.encrypt(password, salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        });

        // 保存数据
        userService.save(users);
        return ResultVoUtil.success("修改成功");
    }

    /**
     * 跳转到角色分配页面
     */
    @GetMapping("/role")
    public String toRole(@RequestParam(value = "ids") SysUser user, Model model) {
        /*// 获取指定用户角色列表
        Set<SysRole> authRoles = user.getSysRoles();
        // 获取全部角色列表
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        List<SysRole> list = roleService.getListBySortOk(sort);*/

        model.addAttribute("id", user.getId());
        model.addAttribute("list", null);
        model.addAttribute("authRoles", null);
        return "system/user/role";
    }

    /* */

    /**
     * 保存角色分配信息
     */
    @PostMapping("/role")
    @ResponseBody
    public SysResultVo auth(
            @RequestParam(value = "id", required = true) SysUser user,
            @RequestParam(value = "roleId", required = false) HashSet<SysRole> sysRoles) {

        // 不允许操作超级管理员数据
        if (user.getId().equals(AdminConst.ADMIN_ID) &&
                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
        }

        // 更新用户角色
        user.setSysRoles(sysRoles);

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }


    /**
     * 获取用户头像
     */
    @GetMapping("/picture")
    public void picture(String p, HttpServletResponse response) throws IOException {
        String defaultPath = "/images/user-picture.jpg";
        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
            String fuPath = properties.getFilePath();
            String spPath = properties.getStaticPath().replace("*", "");
            File file = new File(fuPath + p.replace(spPath, ""));
            if (file.exists()) {
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                return;
            }
        }
        Resource resource = new ClassPathResource("static" + defaultPath);
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @ResponseBody
    public SysResultVo updateStatus(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {

        // 不能修改超级管理员状态
        if (ids.contains(AdminConst.ADMIN_ID)) {
            throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
        }

        // 更新状态
        SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
        if (userService.updateStatus(systemDataStatusEnum, ids)) {
            return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
        }
    }

}
