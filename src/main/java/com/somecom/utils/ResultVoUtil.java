package com.somecom.utils;

import com.somecom.enums.ResultEnum;
import com.somecom.vo.SysResultVo;

/**
 * 响应数据(结果)最外层对象工具
 *
 * @author Sam
 * @date 2018/10/15
 */
public class ResultVoUtil {

    public static SysResultVo SAVE_SUCCESS = success("保存成功");

    /**
     * 操作成功
     *
     * @param msg    提示信息
     * @param object 对象
     */
    public static <T> SysResultVo<T> success(String msg, T object) {
        SysResultVo<T> sysResultVo = new SysResultVo<>();
        sysResultVo.setMsg(msg);
        sysResultVo.setCode(ResultEnum.SUCCESS.getCode());
        sysResultVo.setData(object);
        return sysResultVo;
    }

    /**
     * 操作成功，使用默认的提示信息
     *
     * @param object 对象
     */
    public static <T> SysResultVo<T> success(T object) {
        String message = ResultEnum.SUCCESS.getMessage();
        return success(message, object);
    }

    /**
     * 操作成功，返回提示信息，不返回数据
     */
    public static <T> SysResultVo<T> success(String msg) {
        return success(msg, null);
    }

    /**
     * 操作成功，不返回数据
     */
    public static SysResultVo success() {
        return success(null);
    }

    /**
     * 操作有误
     *
     * @param code 错误码
     * @param msg  提示信息
     */
    public static SysResultVo error(Integer code, String msg) {
        SysResultVo sysResultVo = new SysResultVo();
        sysResultVo.setMsg(msg);
        sysResultVo.setCode(code);
        return sysResultVo;
    }

    /**
     * 操作有误，使用默认400错误码
     *
     * @param msg 提示信息
     */
    public static SysResultVo error(String msg) {
        Integer code = ResultEnum.ERROR.getCode();
        return error(code, msg);
    }

    /**
     * 操作有误，只返回默认错误状态码
     */
    public static SysResultVo error() {
        return error(null);
    }

}
