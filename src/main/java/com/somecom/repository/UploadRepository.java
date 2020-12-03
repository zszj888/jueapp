package com.somecom.repository;

import com.somecom.entity.SysFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/11/02
 */
public interface UploadRepository extends BaseRepository<SysFile, Long>, JpaSpecificationExecutor<SysFile> {

    List<SysFile> findByFreeIsAndMimeIs(boolean free, String mime);

    List<SysFile> findByFreeIsAndCatalogIs(boolean free, String title);

    /**
     * 查找指定文件sha1记录
     *
     * @param sha1 文件sha1值
     * @return 文件信息
     */
    SysFile findBySha1(String sha1);


    SysFile findByName(String name);
/*
    Page<SysFile> findAll(Specification<SysFile> sysFileSpecification, PageRequest page);*/
}

