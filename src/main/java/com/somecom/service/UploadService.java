package com.somecom.service;


import com.somecom.entity.SysFile;
import com.somecom.enums.SystemDataStatusEnum;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/11/02
 */
public interface UploadService {
    Page<SysFile> allFiles(SysFile example);

    List<SysFile> findByFreeIsAndCatalogIs(boolean free, String title);

    /**
     * 获取文件sha1值的记录
     *
     * @param sha1 文件sha1值
     * @return 文件信息
     */
    SysFile getBySha1(String sha1);

    /**
     * 保存文件上传
     *
     * @param upload 文件上传实体类
     * @return 文件信息
     */
    SysFile save(SysFile upload);

    List<SysFile> findVideo(boolean free);

    SysFile findVideoByTitle(String title);

    SysFile getById(Long id);

    Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> ids);
}

