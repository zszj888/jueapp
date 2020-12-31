package com.somecom.web;

import com.somecom.consts.AdminConst;
import com.somecom.entity.Menu;
import com.somecom.entity.SysRole;
import com.somecom.entity.SysUser;
import com.somecom.enums.MenuTypeEnum;
import com.somecom.enums.ResultEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.exception.ResultException;
import com.somecom.service.SysMenuService;
import com.somecom.service.UserService;
import com.somecom.shiro.ShiroUtil;
import com.somecom.utils.EntityBeanUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.validator.UserValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 后台主体内容
     */
    @GetMapping("/admin")
    public String main(Model model) {
        // 获取当前登录的用户
        SysUser user = ShiroUtil.getSubject();
        if (Objects.isNull(user)) {
            return "redirect:/admin/login";
        }
        // 菜单键值对(ID->菜单)
        Map<Long, Menu> keyMenu = new HashMap<>(16);

        // 管理员实时更新菜单
        if (AdminConst.ADMIN_ID.equals(user.getId())) {
            List<Menu> menus = sysMenuService.getListBySortOk();
            menus.forEach(menu -> keyMenu.put(menu.getId(), menu));
        } else {
            // 其他用户需从相应的角色中获取菜单资源
            Set<SysRole> sysRoles = ShiroUtil.getSubjectRoles();
            sysRoles.forEach(role -> {
                role.getMenus().forEach(menu -> {
                    if (menu.getStatus().equals(SystemDataStatusEnum.OK.getCode())) {
                        keyMenu.put(menu.getId(), menu);
                    }
                });
            });
        }

        // 封装菜单树形数据
        Map<Long, Menu> treeMenu = new HashMap<>(16);
        keyMenu.forEach((id, menu) -> {
            if (!menu.getType().equals(MenuTypeEnum.BUTTON.getCode())) {
                if (keyMenu.get(menu.getPid()) != null) {
                    keyMenu.get(menu.getPid()).getChildren().put(Long.valueOf(menu.getSort()), menu);
                } else {
                    if (menu.getType().equals(MenuTypeEnum.DIRECTORY.getCode())) {
                        treeMenu.put(Long.valueOf(menu.getSort()), menu);
                    }
                }
            }
        });

        model.addAttribute("user", user);
        model.addAttribute("treeMenu", treeMenu);
        return "main";
    }

    /**
     * 主页
     */
    @GetMapping("/index")
    public String index(Model model) {

        return "system/main/index";
    }


    /**
     * 跳转到个人信息页面
     */
    @GetMapping("/userInfo")
    public String toUserInfo(Model model) {
        SysUser user = ShiroUtil.getSubject();
        model.addAttribute("user", user);
        return "system/main/userInfo";
    }

    /*
     * 修改用户头像
     *//*
    @PostMapping("/userPicture")
    @ResponseBody
    public SysResultVo userPicture(@RequestParam("picture") MultipartFile picture) {
        UploadController uploadController = SpringContextUtil.getBean(UploadController.class);
        SysResultVo imageResult = uploadController.uploadPicture(picture);
        if (imageResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            SysUser subject = ShiroUtil.getSubject();
            subject.setPicture(((SysFile) imageResult.getData()).getPath());
            userService.save(subject);
            return ResultVoUtil.SAVE_SUCCESS;
        } else {
            return imageResult;
        }
    }*/

    /**
     * 保存修改个人信息
     */
    @PostMapping("/userInfo")
    @ResponseBody
    public SysResultVo userInfo(@Validated UserValid valid, SysUser user) throws MalformedURLException {

        // 复制保留无需修改的数据
        SysUser subUser = ShiroUtil.getSubject();
        String[] ignores = {"id", "username", "password", "salt", "picture", "dept", "sysRoles"};
        EntityBeanUtil.copyPropertiesIgnores(user, subUser, ignores);

        // 保存数据
        userService.save(subUser);
        HashMap<String, Object> spec = new HashMap<>();
        spec.put("url", "/userInfo");
        return ResultVoUtil.success("保存成功", spec);
    }

    /**
     * 跳转到修改密码页面
     */
    @GetMapping("/editPwd")
    public String toEditPwd() {
        return "system/main/editPwd";
    }

    /**
     * 保存修改密码
     */
    @PostMapping("/editPwd")
    @ResponseBody
    public SysResultVo editPwd(String original, String password, String confirm) {
        // 判断原来密码是否有误
        SysUser subUser = ShiroUtil.getSubject();
        String oldPwd = ShiroUtil.encrypt(original, subUser.getSalt());
        if (original.isEmpty() || "".equals(original.trim()) || !oldPwd.equals(subUser.getPassword())) {
            throw new ResultException(ResultEnum.USER_OLD_PWD_ERROR);
        }

        // 判断密码是否为空
        if (password.isEmpty() || "".equals(password.trim())) {
            throw new ResultException(ResultEnum.USER_PWD_NULL);
        }

        // 判断两次密码是否一致
        if (!password.equals(confirm)) {
            throw new ResultException(ResultEnum.USER_INEQUALITY);
        }

        // 修改密码，对密码进行加密
        String salt = ShiroUtil.getRandomSalt();
        String encrypt = ShiroUtil.encrypt(password, salt);
        subUser.setPassword(encrypt);
        subUser.setSalt(salt);

        // 保存数据
        userService.save(subUser);
        return ResultVoUtil.success("修改成功");
    }
}
