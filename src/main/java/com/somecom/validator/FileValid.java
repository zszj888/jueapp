package com.somecom.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Data
public class FileValid implements Serializable {
    @NotEmpty(message = "文件分类必填")
    private String catalog;
    @NotEmpty(message = "视频是否免费必填")
    private String free;
}
