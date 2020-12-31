package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.StatusUtil;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@SQLDelete(sql = "update sys_user" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String salt;
    private String picture;
    private Boolean broker;
    private Byte sex;
    private String email;
    private String phone;
    private String address;
    private String remark;
    @Column(name = "current_month_money")
    private BigDecimal currentMonthMoney;
    @Column(name = "last_month_money")
    private BigDecimal lastMonthMoney;
    @CreatedDate
    @Column(name = "create_date")
    private Timestamp createDate;
    @LastModifiedDate
    @Column(name = "update_date")
    private Timestamp updateDate;
    private Byte status = SystemDataStatusEnum.OK.getCode();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<SysRole> sysRoles = new HashSet<>(0);
    @Column(name = "real_name_auth", columnDefinition = "tinyint unsigned default 0")
    //0：未实名，1：已实名，2：待审核
    private Integer realNameAuth;
    @Column(name = "picture_url")
    private String pictureUrl;
    @Column(name = "open_id")
    private String openId;

    public static SysUser of(String userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(Long.valueOf(userId));
        return sysUser;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getBroker() {
        return broker;
    }

    public void setBroker(Boolean broker) {
        this.broker = broker;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getCurrentMonthMoney() {
        return currentMonthMoney;
    }

    public void setCurrentMonthMoney(BigDecimal currentMonthMoney) {
        this.currentMonthMoney = currentMonthMoney;
    }

    public BigDecimal getLastMonthMoney() {
        return lastMonthMoney;
    }

    public void setLastMonthMoney(BigDecimal lastMonthMoney) {
        this.lastMonthMoney = lastMonthMoney;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Set<SysRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(Set<SysRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

    public Integer getRealNameAuth() {
        return realNameAuth;
    }

    public void setRealNameAuth(Integer realNameAuth) {
        this.realNameAuth = realNameAuth;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
