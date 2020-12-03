package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.StatusUtil;
import lombok.Data;
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
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
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
    private Byte sex;
    private String email;
    private String phone;
    private String address;
    private String remark;
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

    public static SysUser of(String userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(Long.valueOf(userId));
        return sysUser;
    }
}
