package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.Dict;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repository.DictRepository;
import com.somecom.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictRepository dictRepository;

    /**
     * 根据字典ID查询字典数据
     *
     * @param id 字典ID
     */
    @Override
    @Transactional
    public Dict getById(Long id) {
        return dictRepository.findById(id).orElse(null);
    }

    /**
     * 根据字典标识获取字典数据
     *
     * @param name 字典标识
     */
    @Override
    public Dict getByNameOk(String name) {
        return dictRepository.findByNameAndStatus(name, SystemDataStatusEnum.OK.getCode());
    }

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Dict> getPageList(Example<Dict> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return dictRepository.findAll(example, page);
    }

    /**
     * 字典标识是否重复
     *
     * @param dict 字典实体类
     */
    @Override
    public boolean repeatByName(Dict dict) {
        Long id = dict.getId() != null ? dict.getId() : Long.MIN_VALUE;
        return dictRepository.findByNameAndIdNot(dict.getName(), id) != null;
    }

    /**
     * 保存字典
     *
     * @param dict 字典实体类
     */
    @Override
    public Dict save(Dict dict) {
        return dictRepository.save(dict);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> idList) {
        return dictRepository.updateStatus(systemDataStatusEnum.getCode(), idList) > 0;
    }


}
