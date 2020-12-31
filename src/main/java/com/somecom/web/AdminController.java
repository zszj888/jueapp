package com.somecom.web;

import com.somecom.config.ProjectProperties;
import com.somecom.entity.SysUser;
import com.somecom.enums.ResultEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.exception.ResultException;
import com.somecom.service.SysRoleService;
import com.somecom.service.UserService;
import com.somecom.util.SystemUtil;
import com.somecom.utils.CaptchaUtil;
import com.somecom.utils.EncryptUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.SpringContextUtil;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/admin")
public class AdminController implements ErrorController {

    private final SysRoleService sysRoleService;
    @Autowired
    private UserService userService;

    public AdminController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String toLogin(Model model) {
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        model.addAttribute("isCaptcha", properties.isCaptchaOpen());
        return "login";
    }

    /**
     * 实现登录
     */
    @PostMapping("/login")
    @ResponseBody
    public SysResultVo login(String username, String password, String captcha, String rememberMe, HttpSession session) throws MalformedURLException {
        // 判断账号密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ResultException(ResultEnum.USER_NAME_PWD_NULL);
        }

        // 判断验证码是否正确
        ProjectProperties properties = SpringContextUtil.getBean(ProjectProperties.class);
        if (properties.isCaptchaOpen()) {
            String sessionCaptcha = (String) session.getAttribute("captcha");
            if (StringUtils.isEmpty(captcha) || StringUtils.isEmpty(sessionCaptcha)
                    || !captcha.toUpperCase().equals(sessionCaptcha.toUpperCase())) {
                throw new ResultException(ResultEnum.USER_CAPTCHA_ERROR);
            }
            session.removeAttribute("captcha");
        }

        // 获取数据库中的用户名密码
        SysUser user = userService.getByName(username);

        // 判断用户名是否存在
        if (user == null) {
            return ResultVoUtil.error("用户不存在");
        } else if (user.getStatus().equals(SystemDataStatusEnum.FREEZED.getCode())) {
            return ResultVoUtil.error("账户已冻结");
        }

        // 对盐进行加密处理
        String salt = user.getSalt();
        if (!user.getPassword().equals(EncryptUtil.encrypt(password, salt))) {
            return ResultVoUtil.error("密码错误");
        }
        SystemUtil.adminLogin(user);
//        // 1.获取Subject主体对象
//        Subject subject = SecurityUtils.getSubject();
//
//        // 2.封装用户数据
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//
//        // 3.执行登录，进入自定义Realm类中
//        try {
//            // 判断是否自动登录
//            if (rememberMe != null) {
//                token.setRememberMe(true);
//            } else {
//                token.setRememberMe(false);
//            }
//            subject.login(token);

        // 判断是否拥有后台角色
//            SysUser user = ShiroUtil.getSubject();
        if (!sysRoleService.existsUserOk(user.getId())) {
            return ResultVoUtil.error("您不是后台管理员！");
        }
        HashMap<String, String> spec = new HashMap<>(1);
        spec.put("url", "/admin");
        return ResultVoUtil.success("登录成功", spec);
    }

    /**
     * 验证码图片
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应头信息，通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        response.setContentType("image/jpeg");

        // 获取验证码
        String code = CaptchaUtil.getRandomCode();
        // 将验证码输入到session中，用来验证
        request.getSession().setAttribute("captcha", code);
        // 输出到web页面
        ImageIO.write(CaptchaUtil.genCaptcha(code), "jpg", response.getOutputStream());
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout() {
        SystemUtil.adminLogout();
        return "redirect:login";
    }

    /**
     * 权限不足页面
     */
    @GetMapping("/noAuth")
    public String noAuth() {
        return "system/main/noAuth";
    }

    /**
     * 自定义错误页面
     */
    @Override
    public String getErrorPath() {
        return "error";
    }

    /**
     * 处理错误页面
     */
    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMsg = "好像出错了呢！";
        if (statusCode == 404) {
            errorMsg = "页面找不到了！好像是去火星了~";
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("msg", errorMsg);
        return "system/main/error";
    }
}
