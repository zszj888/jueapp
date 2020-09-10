package com.somecom.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String name;

    @NotBlank(message = "图片文件名必传，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "img")
    private String img;

    @NotBlank(message = "文件uri路径，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "imgUrl")
    private String imgUrl;

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
    private LocalDateTime createTime;

    @Column(name = "open_id")
    private String openid;

    @Column(name = "video_url")
    private String video_url;

    @Column(name = "role_type")
    private String role_type;

    @Column(name = "birthda_day")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthda_day;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public LocalDate getBirthda_day() {
        return birthda_day;
    }

    public void setBirthda_day(LocalDate birthda_day) {
        this.birthda_day = birthda_day;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
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
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("img", img)
                .add("imgUrl", imgUrl)
                .add("desc", desc)
                .add("remrks", remrks)
                .add("age", age)
                .add("sex", sex)
                .add("skill", skill)
                .add("isCollection", isCollection)
                .add("createTime", createTime)
                .add("openid", openid)
                .add("video_url", video_url)
                .add("role_type", role_type)
                .add("birthda_day", birthda_day)
                .toString();
    }
}
