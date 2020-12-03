package com.somecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "t_taskapply")
public class TaskApply extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "taskid", referencedColumnName = "id")
    protected Task task;
    @Column(name = "userid")
    private Integer userId;

    public static TaskApply example(Integer userId) {
        TaskApply task = new TaskApply();
        task.setUserId(userId);
        return task;
    }

    public static TaskApply of(Integer taskId) {
        TaskApply task = new TaskApply();
        task.setTask(Task.of(taskId));
        return task;
    }
}
