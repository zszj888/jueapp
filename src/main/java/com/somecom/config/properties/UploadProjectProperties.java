package com.somecom.config.properties;

import com.somecom.utils.ToolUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

/**
 * 项目-文件上传配置项
 *
 * @author Sam
 * @date 2018/11/6
 */
@Data
@Component
@ConfigurationProperties(prefix = "project.upload")
public class UploadProjectProperties {

    /**
     * 上传文件路径
     */
    private String filePath;

    /**
     * 上传文件静态访问路径
     */
    private String staticPath = "/upload/**";

    /**
     * 获取文件路径
     */
    public String getFilePath() {
        return Paths.get(ToolUtil.getProjectPath()).getRoot() + "/upload/";
    }
}
