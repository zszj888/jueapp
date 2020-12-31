//package com.somecom.shiro;
//
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.filter.AccessControlFilter;
//import org.apache.shiro.web.util.WebUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 处理session超时问题拦截器
// *
// * @author Sam
// * @date 2018/8/14
// */
//public class UserAuthFilter extends AccessControlFilter {
//    private final Logger logger = LoggerFactory.getLogger(UserAuthFilter.class);
//
//    @Override
//    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
//        if (isLoginRequest(request, response)) {
//            logger.info("isLoginRequest");
//            return true;
//        } else {
//            Subject subject = getSubject(request, response);
//            logger.info("subject.getPrincipal() != null");
//            return subject.getPrincipal() != null;
//        }
//    }
//
//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpRequest = WebUtils.toHttp(request);
//        HttpServletResponse httpResponse = WebUtils.toHttp(response);
//
//        if (httpRequest.getHeader("X-Requested-With") != null
//                && "XMLHttpRequest".equalsIgnoreCase(httpRequest.getHeader("X-Requested-With"))) {
//            logger.info("httpResponse.sendError(HttpStatus.UNAUTHORIZED.value())");
//            httpResponse.sendError(HttpStatus.UNAUTHORIZED.value());
//        } else {
//            logger.info("redirectToLogin(request, response);");
//            redirectToLogin(request, response);
//        }
//        logger.info("return false;");
//        return false;
//    }
//}
