package com.somecom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "role_name")
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "role_desc")
    private String desc;

    @Column(name = "remrks")
    private String remrks;

    @Column(name = "age")
    private Integer age;
    @Column(name = "sex")
    private Integer sex;

    @Column(name = "skill")
    private String skill;

    @Column(name = "favor")
    private String isCollection;

    @Column(name = "create_time")
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", desc='" + desc + '\'' +
                ", remrks='" + remrks + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", skill='" + skill + '\'' +
                ", isCollection='" + isCollection + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
