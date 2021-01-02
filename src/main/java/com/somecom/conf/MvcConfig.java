package com.somecom.conf;

import com.somecom.entity.SysUser;
import com.somecom.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(MvcConfig.class);
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/files/**")
                        .addResourceLocations("file:upload/");
            }
        };
    }

    @Override
    public void addInterceptors(InterceptorRegistry addInterceptors) {
        addInterceptors.addInterceptor(new HandlerInterceptor(){
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                //如果request.getHeader("X-Requested-With") 返回的是"XMLHttpRequest"说明就是ajax请求，需要特殊处理
                if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
                    log.info("登陆拦截器1执行,请求地址:" + request.getRequestURI() + "  AJAX请求放行");
                    //不拦截ajax请求
                    return true;
                }
                if ("true".equals(request.getHeader("isWX"))) {
                    log.info("登陆拦截器1执行,请求地址:" + request.getRequestURI() + "  WX请求放行");
                    //不拦截微信请求
                    return true;
                }
                log.info("登陆拦截器1执行,请求地址:" + request.getRequestURI());

                //获取session中的user
                SysUser user = (SysUser) request.getSession().getAttribute(SystemUtil.CURRENT_ADMIN);
                //如果为空
                if (user == null) {
                    log.info("当前用户未登录,跳转登录页面");
                    //重定向到登陆页
                    response.sendRedirect("/admin/login"); // 通过接口跳转登录页面, 注:重定向后下边的代码还会执行 ; /outToLogin是跳至登录页的后台接口
                    return false;
                } else {
                    log.info("当前用户已登录，登录的用户名为： " + user.getUsername());
                }
                return true;
            }

        }).addPathPatterns("/**")
                .excludePathPatterns("/") // 排除127.0.0.1进入登录页
                .excludePathPatterns("/api/**") // 排除验证账号密码接口
                .excludePathPatterns("/admin/login/**")
                .excludePathPatterns("/ws/msg/**")
                .excludePathPatterns("/lib/**") // 排除静态文件
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/files/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/font-awesome/**")
                .excludePathPatterns("/favor/**")
                .excludePathPatterns("/wx/**")
                .excludePathPatterns("/pay/**")
                .excludePathPatterns("/role/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/task/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/websocket/**")
                .excludePathPatterns("/css/**");
        ;
    }
}
