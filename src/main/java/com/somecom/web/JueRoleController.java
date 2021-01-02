package com.somecom.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.somecom.entity.Role;
import com.somecom.entity.SysUser;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.model.UploadDataListener;
import com.somecom.service.JueRoleService;
import com.somecom.util.SystemUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/juerole")
public class JueRoleController {

    @Autowired
    private JueRoleService jueRoleService;

    private List<Role> data() {
        /*Optional<SysUser> optionalSysUser = SystemUtil.currentAdmin();
        optionalSysUser.orElseThrow(() -> new IllegalArgumentException("请登录"));
        SysUser user = optionalSysUser.get();
        Role example = new Role();
        if (user.getId() != 1) {
            example.setCreateById(user.getId().intValue());
        }
        return jueRoleService.findAll(example);*/
        return Collections.singletonList(new Role());
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("我的角er", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Role.class).sheet(DateTimeFormatter.ISO_DATE.format(LocalDate.now()))
                .doWrite(data());
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("export")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("我的角er", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), Role.class).autoCloseStream(Boolean.FALSE).sheet(DateTimeFormatter.ISO_DATE.format(LocalDate.now()))
                    .doWrite(data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("upload")
    @ResponseBody
    public SysResultVo upload(MultipartFile file) throws IOException {
        Optional<SysUser> optionalSysUser = SystemUtil.currentAdmin();
        optionalSysUser.orElseThrow(() -> new IllegalArgumentException("请登录"));
        EasyExcel.read(file.getInputStream(), Role.class,
                new UploadDataListener(jueRoleService, optionalSysUser.get().getId().intValue())).sheet().doRead();
        return ResultVoUtil.success("上传成功");
    }

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, Role role) {
        Optional<SysUser> optionalSysUser = SystemUtil.currentAdmin();
        optionalSysUser.orElseThrow(() -> new IllegalArgumentException("请登录"));
        SysUser user = optionalSysUser.get();
        if (user.getId() != 1) {
            role.setCreateById(user.getId().intValue());
        }
//        if ("null".equalsIgnoreCase(role.getCreateById()))
        // 获取用户列表
        Page<Role> list = jueRoleService.getPageList(role);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("show", "admin".equalsIgnoreCase(user.getUsername()));
        model.addAttribute("page", list);
        return "system/juerole/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return "system/juerole/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", jueRoleService.getById(id));
        return "system/juerole/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param role  实体对象
     */
    @PostMapping("/save")
    @ResponseBody
    public SysResultVo save(@Validated UserValid valid, Role role) {

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
//            Role beUser = userService.getById(role.getId());
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
        model.addAttribute("user", jueRoleService.getById(id));
        return "system/juerole/detail";
    }

//
//    /**
//     * 修改密码
//     */
//    @PostMapping("/pwd")
//    @ResponseBody
//    public SysResultVo editPassword(String password, String confirm,
//                                    @RequestParam(value = "ids", required = false) List<Long> ids,
//                                    @RequestParam(value = "ids", required = false) List<Role> users) {
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
//    public String toRole(@RequestParam(value = "ids") Role user, Model model) {
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
//            @RequestParam(value = "id", required = true) Role user,
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
        if (jueRoleService.updateStatus(systemDataStatusEnum, ids)) {
            return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
        }
    }

}
