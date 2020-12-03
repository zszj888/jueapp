//package com.somecom.exception;
//
//import com.somecom.exception.advice.ResultExceptionAdvice;
//import com.somecom.utils.ResultVoUtil;
//import com.somecom.utils.SpringContextUtil;
//import com.somecom.vo.SysResultVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.BindException;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Objects;
//
///**
// * 全局统一异常处理
// *
// * @author 小懒虫
// * @date 2018/8/14
// */
//@ControllerAdvice
//@Slf4j
//public class ResultExceptionHandler {
//
//    /**
//     * 拦截自定义异常
//     */
//    @ExceptionHandler(ResultException.class)
//    @ResponseBody
//    public SysResultVo resultException(ResultException e) {
//        return ResultVoUtil.error(e.getCode(), e.getMessage());
//    }
//
//    /**
//     * 拦截表单验证异常
//     */
//    @ExceptionHandler(BindException.class)
//    @ResponseBody
//    public SysResultVo bindException(BindException e) {
//        BindingResult bindingResult = e.getBindingResult();
//        return ResultVoUtil.error(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
//    }
//
//    /**
//     * 拦截未知的运行时异常
//     */
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    public SysResultVo runtimeException(RuntimeException e) {
//        ResultExceptionAdvice resultExceptionAdvice = SpringContextUtil.getBean(ResultExceptionAdvice.class);
//        resultExceptionAdvice.runtimeException(e);
//        log.error("【系统异常】", e);
//        return ResultVoUtil.error(500, "未知错误：EX4399");
//    }
//}
