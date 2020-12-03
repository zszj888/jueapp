package com.somecom.service;

import com.somecom.entity.Task;
import org.springframework.data.domain.Page;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface TaskService {

    /**
     * 获取分页列表数据
     *
     * @param user 实体对象
     * @return 返回分页数据
     */
    Page<Task> getPageList(Task user);

    /**
     * 根据用户ID查询用户数据
     *
     * @param id 用户ID
     * @return 用户信息
     */
    Task getById(Long id);

}
