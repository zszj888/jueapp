package com.somecom.validator;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Data
public class NewsValid implements Serializable {
    @NotEmpty(message = "新闻标题不能为空")
    private String title;
    @NotEmpty(message = "链接地址不能为空")
    @Size(min = 4, message = "链接地址：请输入至少4个字符")
    private String href;
}
