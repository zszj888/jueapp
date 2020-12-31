package com.somecom.vo;

import lombok.Data;

/**
 * 响应数据(结果)最外层对象
 *
 * @author Sam
 * @date 2018/10/15
 */
@Data
public class SysResultVo<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;
}
