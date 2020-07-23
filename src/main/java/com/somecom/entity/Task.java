package com.somecom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Date createTime;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", desc='" + desc + '\'' +
                ", remrks='" + remrks + '\'' +
                ", ontop=" + ontop +
                ", recommend=" + recommend +
                ", skill='" + skill + '\'' +
                ", isCollection='" + isCollection + '\'' +
                ", createTime=" + createTime +
                '}';
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
