package com.somecom.enums;

import lombok.Getter;

/**
 * 菜单类型枚举
 *
 * @author Sam
 * @date 2018/8/14
 */
@Getter
public enum FileTypeEnum {

    /**
     * video
     */
    VIDEO((byte) 1, "video"),
    /**
     * pic
     */
    JPG((byte) 2, "picture");


    private Byte code;

    private String message;

    FileTypeEnum(Byte code, String message) {
        this.code = code;
        this.message = message;
    }
}

