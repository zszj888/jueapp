package com.somecom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_favor_task")
public class FavoriteTask extends BaseEntity {
    @Column(name = "classify_name")
    private String classifyName;
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public static FavoriteTask ofUser(Integer userId) {
        FavoriteTask favoriteTask = new FavoriteTask();
        favoriteTask.setUserId(userId);
        return favoriteTask;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
