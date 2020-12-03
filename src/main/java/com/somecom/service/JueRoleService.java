package com.somecom.service;

import com.somecom.entity.Role;
import com.somecom.enums.SystemDataStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface JueRoleService {

    /**
     * 获取分页列表数据
     *
     * @param user 实体对象
     * @return 返回分页数据
     */
    Page<Role> getPageList(Role user);

    /**
     * 根据用户ID查询用户数据
     *
     * @param id 用户ID
     * @return 用户信息
     */
    Role getById(Long id);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     *
     * @param systemDataStatusEnum 数据状态
     * @param idList               数据ID列表
     * @return 操作结果
     */
    @Transactional
    Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> idList);

}
