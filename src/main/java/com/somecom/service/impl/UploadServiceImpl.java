package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.SysFile;
import com.somecom.enums.FileTypeEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repository.UploadRepository;
import com.somecom.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam
 * @date 2018/11/02
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UploadRepository uploadRepository;

    public UploadServiceImpl(UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
    }

    @Override
    public Page<SysFile> allFiles(SysFile example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.DESC);
        // 使用Specification复杂查询
        return uploadRepository.findAll((Specification<SysFile>) (root, query, cb) -> {
            List<Predicate> preList = new ArrayList<>();
            if (example.getId() != null) {
                preList.add(cb.equal(root.get("id").as(Long.class), example.getId()));
            }
            if (example.getName() != null) {
                preList.add(cb.equal(root.get("name").as(String.class), example.getName()));
            }
            if (example.getPath() != null) {
                preList.add(cb.like(root.get("path").as(String.class), example.getPath()));
            }
            if (example.getMime() != null) {
                preList.add(cb.like(root.get("mime").as(String.class), example.getMime()));
            }
            // 数据状态
            if (example.getStatus() != null) {
                preList.add(cb.equal(root.get("status").as(Byte.class), example.getStatus()));
            }
            Predicate[] pres = new Predicate[preList.size()];
            return query.where(preList.toArray(pres)).getRestriction();
        }, page);
    }

    @Override
    public List<SysFile> findByFreeIsAndCatalogIs(boolean free, String title) {
        return uploadRepository.findByFreeIsAndCatalogIs(free, title);
    }

    public List<SysFile> findVideo(boolean free) {
        return uploadRepository.findByFreeIsAndMimeIs(true, String.valueOf(FileTypeEnum.VIDEO.getCode()));
    }

    @Override
    public SysFile getById(Long id) {
        return uploadRepository.getOne(id);
    }

    @Override
    public SysFile findVideoByTitle(String title) {
        SysFile sysFile = new SysFile();
        sysFile.setName(StringUtils.trimAllWhitespace(title));
        try {
            return uploadRepository.findOne(Example.of(sysFile)).get();
        } catch (IncorrectResultSizeDataAccessException e) {
            return new SysFile();
        }
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> ids) {
        // 联级删除与角色之间的关联
        if (systemDataStatusEnum == SystemDataStatusEnum.DELETE) {
            return uploadRepository.deleteByIdIn(ids) > 0;
        }
        return uploadRepository.updateStatus(systemDataStatusEnum.getCode(), ids) > 0;
    }

    /**
     * 获取文件sha1值的记录
     */
    @Override
    public SysFile getBySha1(String sha1) {
        return uploadRepository.findBySha1(sha1);
    }

    /**
     * 保存文件上传
     *
     * @param upload 文件上传实体类
     */
    @Override
    public SysFile save(SysFile upload) {
        return uploadRepository.save(upload);
    }
}

