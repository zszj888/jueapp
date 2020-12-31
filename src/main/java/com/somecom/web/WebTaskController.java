package com.somecom.web;

import com.somecom.entity.Task;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.service.TaskService;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.StatusUtil;
import com.somecom.validator.UserValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/task")
public class WebTaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, Task role) {

        // 获取用户列表
        Page<Task> list = taskService.getPageList(role);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "system/task/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return "system/task/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", taskService.getById(id));
        return "system/task/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param role  实体对象
     */
    @PostMapping("/save")
    @ResponseBody
    public SysResultVo save(@Validated UserValid valid, Task role) {

        // 验证数据是否合格
//        if (role.getId() == null) {
//
//            // 判断密码是否为空
//            if (role.getPassword().isEmpty() || "".equals(role.getPassword().trim())) {
//                throw new ResultException(ResultEnum.USER_PWD_NULL);
//            }
//
//            // 判断两次密码是否一致
//            if (!role.getPassword().equals(valid.getConfirm())) {
//                throw new ResultException(ResultEnum.USER_INEQUALITY);
//            }
//
//            // 对密码进行加密
//            String salt = EncryptUtil.getRandomSalt();
//            String encrypt = EncryptUtil.encrypt(role.getPassword(), salt);
//            role.setPassword(encrypt);
//            role.setSalt(salt);
//        }
//
//        // 判断用户名是否重复
//        if (userService.repeatByUsername(role)) {
//            throw new ResultException(ResultEnum.USER_EXIST);
//        }
//
//        // 复制保留无需修改的数据
//        if (role.getId() != null) {
//            // 不允许操作超级管理员数据
//            if (role.getId().equals(AdminConst.ADMIN_ID)
//                /*  &&!ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)*/) {
//                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
//            }
//
//            Task beUser = userService.getById(role.getId());
//            String[] fields = {"password", "salt", "picture", "sysRoles"};
//            EntityBeanUtil.copyProperties(beUser, role, fields);
//        }
//
//        // 保存数据
//        userService.save(role);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", taskService.getById(id));
        return "system/task/detail";
    }

//
//    /**
//     * 修改密码
//     */
//    @PostMapping("/pwd")
//    @ResponseBody
//    public SysResultVo editPassword(String password, String confirm,
//                                    @RequestParam(value = "ids", required = false) List<Long> ids,
//                                    @RequestParam(value = "ids", required = false) List<Task> users) {
//
//        // 判断密码是否为空
//        if (password.isEmpty() || "".equals(password.trim())) {
//            throw new ResultException(ResultEnum.USER_PWD_NULL);
//        }
//
//        // 判断两次密码是否一致
//        if (!password.equals(confirm)) {
//            throw new ResultException(ResultEnum.USER_INEQUALITY);
//        }
//
//        // 不允许操作超级管理员数据
//        if (ids.contains(AdminConst.ADMIN_ID) /*&&
//                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)*/) {
//            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
//        }
//
//        // 修改密码，对密码进行加密
//        users.forEach(user -> {
//            String salt = EncryptUtil.getRandomSalt();
//            String encrypt = EncryptUtil.encrypt(password, salt);
//            user.setPassword(encrypt);
//            user.setSalt(salt);
//        });
//
//        // 保存数据
//        userService.save(users);
//        return ResultVoUtil.success("修改成功");
//    }
//
//    /**
//     * 跳转到角色分配页面
//     */
//    @GetMapping("/role")
//    public String toRole(@RequestParam(value = "ids") Task user, Model model) {
//        /*// 获取指定用户角色列表
//        Set<SysRole> authRoles = user.getSysRoles();
//        // 获取全部角色列表
//        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
//        List<SysRole> list = roleService.getListBySortOk(sort);*/
//
//        model.addAttribute("id", user.getId());
//        model.addAttribute("list", null);
//        model.addAttribute("authRoles", null);
//        return "system/user/role";
//    }
//
//    /* */
//
//    /**
//     * 保存角色分配信息
//     */
//    @PostMapping("/role")
//    @ResponseBody
//    public SysResultVo auth(
//            @RequestParam(value = "id", required = true) Task user,
//            @RequestParam(value = "roleId", required = false) HashSet<SysRole> sysRoles) {
//
//        // 不允许操作超级管理员数据
//        if (user.getId().equals(AdminConst.ADMIN_ID) &&
//                !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
//            throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
//        }
//
//        // 更新用户角色
//        user.setSysRoles(sysRoles);
//
//        // 保存数据
//        userService.save(user);
//        return ResultVoUtil.SAVE_SUCCESS;
//    }
//
//
//    /**
//     * 获取用户头像
//     */
//    @GetMapping("/picture")
//    public void picture(String p, HttpServletResponse response) throws IOException {
//        String defaultPath = "/images/user-picture.jpg";
//        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
//            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
//            String fuPath = properties.getFilePath();
//            String spPath = properties.getStaticPath().replace("*", "");
//            File file = new File(fuPath + p.replace(spPath, ""));
//            if (file.exists()) {
//                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
//                return;
//            }
//        }
//        Resource resource = new ClassPathResource("static" + defaultPath);
//        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
//    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @ResponseBody
    public SysResultVo updateStatus(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {

        // 不能修改超级管理员状态
//        if (ids.contains(AdminConst.ADMIN_ID)) {
//            throw new ResultException(ResultEnum.NO_ADMIN_STATUS);
//        }

        // 更新状态
        SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
//        if (taskService.updateStatus(systemDataStatusEnum, ids)) {
        return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
//        } else {
//            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
//        }
    }

}
