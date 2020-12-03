package com.somecom.service.impl;

import com.somecom.data.PageSort;
import com.somecom.entity.Task;
import com.somecom.repo.TaskRepository;
import com.somecom.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     */
    @Override
    public Task getById(Long id) {
        return taskRepository.findById(id.intValue()).orElse(null);
    }

    /**
     * 获取分页列表数据
     *
     * @param task 实体对象
     * @return 返回分页数据
     */
    @Override
    @Transactional
    public Page<Task> getPageList(Task task) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest("createTime", Sort.Direction.DESC);

        // 使用Specification复杂查询
        Page<Task> all = taskRepository.findAll((Specification<Task>) (root, query, cb) -> {
            List<Predicate> preList = new ArrayList<>();
            if (task.getId() != null) {
                preList.add(cb.equal(root.get("id").as(Integer.class), task.getId()));
            }
            if (StringUtils.hasText(task.getName())) {
                preList.add(cb.equal(root.get("name").as(String.class), task.getName()));
            }
            if (task.getSkill() != null) {
                preList.add(cb.like(root.get("skill").as(String.class), "%" + task.getSkill() + "%"));
            }

            // 数据状态
            if (task.getStatus() != null) {
                preList.add(cb.equal(root.get("status").as(String.class), task.getStatus()));
            }

            Predicate[] pres = new Predicate[preList.size()];
            return query.where(preList.toArray(pres)).getRestriction();
        }, page);
        return all;
    }

}
