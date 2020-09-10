package com.somecom.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_name")
    private String name;
    @Column(name = "nick_name")
    private String nick_name;
    private String phone;
    private String sex;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthda_day;
    @Column(name = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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

    public User() {
    }

    @ManyToMany
    @JoinTable(name = "USER_FAVOR_TASK", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TASK_ID", referencedColumnName = "ID"))
    private Set<Task> favorTasks;

    @ManyToMany
    @JoinTable(name = "USER_FAVOR_ROLE", joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private Set<Role> favorRoles;

    public User(String openid) {
        this.openid = openid;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public byte getBroker() {
        return broker;
    }

    public void setBroker(byte broker) {
        this.broker = broker;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<Task> getFavorTasks() {
        return favorTasks;
    }

    public void setFavorTasks(Set<Task> favorTasks) {
        this.favorTasks = favorTasks;
    }

    public Set<Role> getFavorRoles() {
        return favorRoles;
    }

    public void setFavorRoles(Set<Role> favorRoles) {
        this.favorRoles = favorRoles;
    }

    public void setBirthda_day(LocalDate birthda_day) {
        this.birthda_day = birthda_day;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("nick_name", nick_name)
                .add("phone", phone)
                .add("sex", sex)
                .add("birthda_day", birthda_day)
                .add("createTime", createTime)
                .add("openid", openid)
                .add("height", height)
                .add("weight", weight)
                .add("picture", pictureUrl)
                .add("talents", talents)
                .add("castHistory", castHistory)
                .add("shortVideoUrl", shortVideoUrl)
                .add("label", label)
                .add("organization", organization)
                .add("balance", balance)
                .add("broker", broker)
                .add("favorTasks", favorTasks)
                .add("favorRoles", favorRoles)
                .toString();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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


    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getBirthda_day() {
        return birthda_day;
    }
}