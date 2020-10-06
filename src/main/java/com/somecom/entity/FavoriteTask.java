package com.somecom.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_favor_task")
public class FavoriteTask extends BaseEntity {
    @Column(name = "classify_name")
    private String classifyName;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "task_id")
    private Integer taskId;
}
