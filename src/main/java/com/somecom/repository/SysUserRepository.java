package com.somecom.repository;

import com.somecom.entity.SysUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Sam
 * @date 2018/8/14
 */
public interface SysUserRepository extends BaseRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {
    SysUser findByPhone(String phone);

    SysUser findByOpenId(String openId);
    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    public SysUser findByUsername(String username);

    /**
     * 根据用户名查询用户数据,且排查指定ID的用户
     *
     * @param username 用户名
     * @param id       排除的用户ID
     * @return 用户数据
     */
    public SysUser findByUsernameAndIdNot(String username, Long id);

}
