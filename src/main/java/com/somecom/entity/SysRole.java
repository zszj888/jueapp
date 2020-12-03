package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.StatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "sys_role")
@ToString(exclude = {"users", "menus", "createBy", "updateBy"})
@EqualsAndHashCode(exclude = {"users", "menus", "createBy", "updateBy"})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update sys_role" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
public class SysRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String remark;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "create_by")
    @JsonIgnore
    private SysUser createBy;
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "update_by")
    @JsonIgnore
    private SysUser updateBy;
    private Byte status = SystemDataStatusEnum.OK.getCode();

    @ManyToMany(mappedBy = "sysRoles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<SysUser> users = new HashSet<>(0);

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id"))
    @JsonIgnore
    private Set<Menu> menus = new HashSet<>(0);

    public static SysRole ofStudent() {
        SysRole sysRole = new SysRole();
        sysRole.setId(2L);
        return sysRole;
    }
}
