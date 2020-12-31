package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.SysRole;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repository.SysRoleRepository;
import com.somecom.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    /**
     * 获取用户角色列表
     *
     * @param id 用户ID
     */
    @Override
    @Transactional
    public Set<SysRole> getUserOkRoleList(Long id) {
        Byte status = SystemDataStatusEnum.OK.getCode();
        return sysRoleRepository.findByUsers_IdAndStatus(id, status);
    }

    /**
     * 判断指定的用户是否存在角色
     *
     * @param id 用户ID
     */
    @Override
    public Boolean existsUserOk(Long id) {
        Byte status = SystemDataStatusEnum.OK.getCode();
        return sysRoleRepository.existsByUsers_IdAndStatus(id, status);
    }

    /**
     * 根据角色ID查询角色数据
     *
     * @param id 角色ID
     */
    @Override
    @Transactional
    public SysRole getById(Long id) {
        return sysRoleRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     *
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<SysRole> getPageList(Example<SysRole> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return sysRoleRepository.findAll(example, page);
    }

    /**
     * 获取角色列表数据
     *
     * @param sort 排序对象
     */
    @Override
    public List<SysRole> getListBySortOk(Sort sort) {
        return sysRoleRepository.findAllByStatus(sort, SystemDataStatusEnum.OK.getCode());
    }

    /**
     * 角色标识是否重复
     *
     * @param sysRole 角色实体类
     */
    @Override
    public boolean repeatByName(SysRole sysRole) {
        Long id = sysRole.getId() != null ? sysRole.getId() : Long.MIN_VALUE;
        return sysRoleRepository.findByNameAndIdNot(sysRole.getName(), id) != null;
    }

    /**
     * 保存角色
     *
     * @param sysRole 角色实体类
     */
    @Override
    public SysRole save(SysRole sysRole) {
        return sysRoleRepository.save(sysRole);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> ids) {
        // 删除角色时取消与角色和菜单的关联
        if (systemDataStatusEnum == SystemDataStatusEnum.DELETE) {
            // 非规范的Jpa操作，直接采用SQL语句
            sysRoleRepository.cancelUserJoin(ids);
            sysRoleRepository.cancelMenuJoin(ids);
        }
        return sysRoleRepository.updateStatus(systemDataStatusEnum.getCode(), ids) > 0;
    }
}
