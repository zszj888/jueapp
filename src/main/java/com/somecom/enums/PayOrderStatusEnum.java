package com.somecom.enums;

import lombok.Getter;

@Getter
public enum PayOrderStatusEnum {

    NEW((byte) 1, "新建"),
    WAIT_FOR_PAY((byte) 2, "等待支付"),
    SUCCESS((byte) 3, "成功"),
    FAIL((byte) 4, "失败");

    private Byte code;

    private String message;

    PayOrderStatusEnum(Byte code, String message) {
        this.code = code;
        this.message = message;
    }
}

