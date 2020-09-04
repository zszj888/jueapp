package com.somecom.entity;

import com.google.common.base.MoreObjects;

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
@Table(name = "t_task")
public class Task {

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("img", img)
                .add("desc", desc)
                .add("remrks", remrks)
                .add("ontop", ontop)
                .add("recommend", recommend)
                .add("skill", skill)
                .add("isCollection", isCollection)
                .add("createTime", createTime)
                .add("status", status)
                .add("taskTime", taskTime)
                .add("role", role)
                .add("validateTime", validateTime)
                .toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemrks() {
        return remrks;
    }

    public void setRemrks(String remrks) {
        this.remrks = remrks;
    }

    public Byte getOntop() {
        return ontop;
    }

    public void setOntop(Byte ontop) {
        this.ontop = ontop;
    }

    public Byte getRecommend() {
        return recommend;
    }

    public void setRecommend(Byte recommend) {
        this.recommend = recommend;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(String isCollection) {
        this.isCollection = isCollection;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.taskTime = taskTime;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getValidateTime() {
        return validateTime;
    }

    public void setValidateTime(LocalDateTime validateTime) {
        this.validateTime = validateTime;
    }
}
