package com.somecom.enums;

import lombok.Getter;

@Getter
public enum BusinessType {

    ROLE((byte) 1, "角色支付"),
    TASK((byte) 2, "任务支付");


    private Byte code;

    private String name;

    BusinessType(Byte code, String name) {
        this.code = code;
        this.name = name;
    }
}

