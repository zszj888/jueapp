//package com.somecom.shiro.exception;
//
//import com.somecom.utils.ResultVoUtil;
//import com.somecom.vo.SysResultVo;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 拦截访问权限异常处理
// *
// * @author 小懒虫
// * @date 2019/4/26
// */
//@ControllerAdvice
//@Order(-1)
//public class AuthorizationExceptionHandler {
//
//    /**
//     * 拦截访问权限异常
//     */
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public SysResultVo authorizationException(Exception e, HttpServletRequest request,
//                                              HttpServletResponse response) {
//        e.printStackTrace();
////        Integer code = ResultEnum.NO_PERMISSIONS.getCode();
////        String msg = ResultEnum.NO_PERMISSIONS.getMessage();
//
//        // 获取异常信息
////        Throwable cause = e.getCause();
////        if (Objects.nonNull(cause)){
////        String message = cause.getMessage();
////        Class<SysResultVo> resultVoClass = SysResultVo.class;
////
////        // 判断无权限访问的方法返回对象是否为ResultVo
////        if (!message.contains(resultVoClass.getName())) {
////            try {
////                //重定向到无权限页面
////                String contextPath = request.getContextPath();
//////                ShiroFilterFactoryBean shiroFilter = SpringContextUtil.getBean(ShiroFilterFactoryBean.class);
////                response.sendRedirect(contextPath);
////            } catch (IOException e1) {
////                return ResultVoUtil.error(code, msg);
////            }
////        }
//        return ResultVoUtil.error(500, e.getMessage());
//    }
//}
