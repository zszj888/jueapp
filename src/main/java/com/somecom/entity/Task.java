package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "t_task")
public class Task extends BaseEntity {

    @Column(name = "task_name")
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "task_desc")
    private String desc;

    @Column(name = "remrks")
    private String remrks;

    @Column(name = "ontop")
    private Byte ontop;
    @Column(name = "recommend")
    private Byte recommend;

    @Column(name = "skill")
    private String skill;

    @Transient
    private Boolean collection;

    @Column(name = "create_time")
    private LocalDateTime createTime;
    private String contact;
    private String phone;
    //已付款待确认/进行中/已完成||已发布>待确认>进行中>已完成
    private String status;
    private String position;
    @Column(name = "task_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime taskTime;
    private BigDecimal fee;
    //有效期
    @Column(name = "validate_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime validateTime;
    @Column(name = "create_by_id")
    private Integer createById;
    @Column(name = "accepted_by_id")
    private Integer acceptedById;
    @Column(name = "apply_persons")
    private String applyPersons;
    @Transient
    private boolean validate;

    public static Task ofNameAndUserId(String name, Integer createBy) {
        Task task = new Task();
        task.setName(name);
        task.setCreateById(createBy);
        return task;
    }

    public static Task of(Integer taskId) {
        Task task = new Task();
        task.setId(taskId);
        return task;
    }

    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }
}
