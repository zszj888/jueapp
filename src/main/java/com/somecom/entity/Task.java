package com.somecom.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data

@Table(name = "t_task")
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "favor")
    private String isCollection;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    //已付款待确认/进行中/已完成
    private String status;
    @Column(name = "task_time")
    private LocalDateTime taskTime;
    @OneToOne
    @JoinColumn(name = "role_id", nullable = false, updatable = false)
    private Role role;

    //有效期
    @Column(name = "validate_time")
    private LocalDateTime validateTime;

    @ManyToOne
    @JoinColumn(name = "produced_by_user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "accepted_by_user_id")
    private User acceptUser;

}
