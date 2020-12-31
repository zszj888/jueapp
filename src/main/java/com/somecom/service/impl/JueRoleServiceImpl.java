package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.Role;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repo.RoleRepository;
import com.somecom.service.JueRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Service
public class JueRoleServiceImpl implements JueRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll(Role exam) {
        return roleRepository.findAll(Example.of(exam));
    }

    @Override
    public void save(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     */
    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id.intValue()).orElse(null);
    }

    /**
     * 获取分页列表数据
     *
     * @param role 实体对象
     * @return 返回分页数据
     */
    @Override
    @Transactional
    public Page<Role> getPageList(Role role) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest("createTime", Sort.Direction.DESC);

        // 使用Specification复杂查询
        Page<Role> all = roleRepository.findAll((Specification<Role>) (root, query, cb) -> {
            List<Predicate> preList = new ArrayList<>();
            if (Objects.nonNull(role.getCreateById())){
                preList.add(cb.equal(root.get("createById").as(Integer.class), role.getCreateById()));
            }
            if (role.getId() != null) {
                preList.add(cb.equal(root.get("id").as(Integer.class), role.getId()));
            }
            if (StringUtils.hasText(role.getName())) {
                preList.add(cb.equal(root.get("name").as(String.class), role.getName()));
            }
            if (role.getSkill() != null) {
                preList.add(cb.like(root.get("skill").as(String.class), "%" + role.getSkill() + "%"));
            }

            // 数据状态
            if (role.getStatus() != null) {
                preList.add(cb.equal(root.get("status").as(String.class), role.getStatus()));
            }

            Predicate[] pres = new Predicate[preList.size()];
            return query.where(preList.toArray(pres)).getRestriction();
        }, page);
        return all;
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(SystemDataStatusEnum systemDataStatusEnum, List<Long> ids) {
        return true;//roleRepository.updateStatus(systemDataStatusEnum.getCode(), ids) > 0;
    }
}
