package com.somecom.service;

import com.somecom.entity.SysUser;
import com.somecom.enums.SystemDataStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface UserService {

    SysUser login(SysUser u);

    /**
     * 获取分页列表数据
     *
     * @param user 实体对象
     * @return 返回分页数据
     */
    Page<SysUser> getPageList(SysUser user);

    /**
     * 保存用户
     *
     * @param user 用户实体类
     * @return 用户信息
     */
    SysUser save(SysUser user);


    /**
     * 保存用户列表
     *
     * @param userList 用户实体类
     * @return 用户列表
     */
    List<SysUser> save(List<SysUser> userList);

    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    SysUser getByName(String username);

    /**
     * 用户名是否重复
     *
     * @param user 用户对象
     * @return 用户数据
     */
    Boolean repeatByUsername(SysUser user);

    /**
     * 根据用户ID查询用户数据
     *
     * @param id 用户ID
     * @return 用户信息
     */
    SysUser getById(Long id);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     *
     * @param systemDataStatusEnum 数据状态
     * @param idList               数据ID列表
     * @return 操作结果
     */
    @Transactional
    Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> idList);

    void updateStudyTotalMin(SysUser user);

}
