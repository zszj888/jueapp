package com.somecom.action.model;

import com.somecom.enums.ActionLogEnum;
import lombok.Getter;

/**
 * @author Sam
 * @date 2018/10/15
 */
@Getter
public class SystemType extends BusinessType {

    /**
     * 日志类型
     */
    protected Byte type = ActionLogEnum.SYSTEM.getCode();

    public SystemType(String message) {
        super(message);
    }

    public SystemType(String name, String message) {
        super(name, message);
    }
}
