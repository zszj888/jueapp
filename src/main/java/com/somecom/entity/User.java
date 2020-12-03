package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@ToString(callSuper = true)
@Entity
@Table(name = "t_user")
public class User extends BaseEntity {
    @Column(name = "talent_role_id")
    private Integer talentId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "nick_name")
    private String nick_name;
    private String phone;
    private String sex;

    private LocalDate birthda_day;
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Column(name = "open_id")
    private String openid;
    private Integer height;
    private Integer weight;
    @Column(name = "picture_url")
    private String pictureUrl;
    private String talents;
    @Column(name = "cast_history")
    private String castHistory;
    @Column(name = "short_video_url")
    private String shortVideoUrl;
    private String label;
    private String organization;
    private BigDecimal balance;
    private byte broker;
    private String bankCard;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    private Set<Task> tasks;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    private Set<Role> roles;

    public User() {
    }

    public User(String openid) {
        this.openid = openid;
    }

    public Integer getTalentId() {
        return talentId;
    }

    public void setTalentId(Integer talentId) {
        this.talentId = talentId;
    }

    public User(Integer userId) {
        this.setId(userId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthda_day() {
        return birthda_day;
    }

    public void setBirthda_day(LocalDate birthda_day) {
        this.birthda_day = birthda_day;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @PrePersist
    public void setCreateTime() {
        this.createTime = LocalDateTime.now();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTalents() {
        return talents;
    }

    public void setTalents(String talents) {
        this.talents = talents;
    }

    public String getCastHistory() {
        return castHistory;
    }

    public void setCastHistory(String castHistory) {
        this.castHistory = castHistory;
    }

    public String getShortVideoUrl() {
        return shortVideoUrl;
    }

    public void setShortVideoUrl(String shortVideoUrl) {
        this.shortVideoUrl = shortVideoUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public byte getBroker() {
        return broker;
    }

    public void setBroker(byte broker) {
        this.broker = broker;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public SysUser toSysUser() {
        SysUser user = new SysUser();
        user.setUsername(this.getName());
        user.setCreateDate(new Timestamp(new Date().getTime()));
        user.setSex(this.sex.equals("man") ? (byte) 1 : 0);
        user.setPhone(this.phone);
        user.setPicture(this.pictureUrl);
        user.setNickname(this.nick_name);
        user.setPassword("666666");
        return user;
    }
}