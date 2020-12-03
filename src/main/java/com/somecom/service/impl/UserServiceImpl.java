package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.SysUser;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repository.SysUserRepository;
import com.somecom.service.UserService;
import com.somecom.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    public static void main(String[] args) {
        String salt = EncryptUtil.getRandomSalt();
        System.out.println(salt);
        System.out.println(EncryptUtil.encrypt("1", salt));
    }

    /**
     * 根据用户名查询用户数据
     *
     * @param username 用户名
     * @return 用户数据
     */
    @Override
    public SysUser getByName(String username) {
        return sysUserRepository.findByUsername(username);
    }

    /**
     * 用户名是否存在
     *
     * @param user 用户对象
     * @return 用户数据
     */
    @Override
    public Boolean repeatByUsername(SysUser user) {
        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
        return sysUserRepository.findByUsernameAndIdNot(user.getUsername(), id) != null;
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     */
    @Override
    public SysUser getById(Long id) {
        return sysUserRepository.findById(id).orElse(null);
    }

    @Override
    public SysUser login(SysUser u) {
        return sysUserRepository.findByPhone(u.getPhone());
    }

    /**
     * 获取分页列表数据
     *
     * @param user 实体对象
     * @return 返回分页数据
     */
    @Override
    @Transactional
    public Page<SysUser> getPageList(SysUser user) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);

        // 使用Specification复杂查询
        Page<SysUser> all = sysUserRepository.findAll((Specification<SysUser>) (root, query, cb) -> {
            List<Predicate> preList = new ArrayList<>();
            if (user.getId() != null) {
                preList.add(cb.equal(root.get("id").as(Long.class), user.getId()));
            }
            if (user.getUsername() != null) {
                preList.add(cb.equal(root.get("username").as(String.class), user.getUsername()));
            }
            if (user.getNickname() != null) {
                preList.add(cb.like(root.get("nickname").as(String.class), "%" + user.getNickname() + "%"));
            }

            // 数据状态
            if (user.getStatus() != null) {
                preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
            }

            Predicate[] pres = new Predicate[preList.size()];
            return query.where(preList.toArray(pres)).getRestriction();
        }, page);
        return all;
    }

    /**
     * 保存用户
     *
     * @param user 用户实体类
     */
    @Override
    public SysUser save(SysUser user) {
        String salt = EncryptUtil.getRandomSalt();
        String encrypt = EncryptUtil.encrypt(user.getPassword(), salt);
        user.setPassword(encrypt);
        user.setSalt(salt);
        if (Objects.nonNull(sysUserRepository.findByPhone(user.getPhone()))) {
            return new SysUser();
        }
        if (Objects.isNull(user.getSex())) {
            user.setSex((byte) 1);
        }
        user.setCreateDate(Timestamp.from(Instant.now()));
        user.setUpdateDate(Timestamp.from(Instant.now()));
        return sysUserRepository.save(user);
    }

    /**
     * 保存用户列表
     *
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<SysUser> save(List<SysUser> userList) {
        List<SysUser> sysUsers = sysUserRepository.saveAll(userList);
        sysUserRepository.flush();
        return sysUsers;
    }

    @Override
    public void updateStudyTotalMin(SysUser user) {
        sysUserRepository.saveAndFlush(user);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> ids) {
        // 联级删除与角色之间的关联
        if (systemDataStatusEnum == SystemDataStatusEnum.DELETE) {
            return sysUserRepository.deleteByIdIn(ids) > 0;
        }
        return sysUserRepository.updateStatus(systemDataStatusEnum.getCode(), ids) > 0;
    }
}
