package com.somecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "t_roleapply")
public class RoleApply extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "roleid", referencedColumnName = "id")
    protected Role role;
    @Column(name = "userid")
    private Integer userId;

    public static RoleApply of(Integer roleId) {
        RoleApply roleApply = new RoleApply();
        roleApply.setRole(Role.of(roleId));
        return roleApply;
    }

    public static RoleApply example(Integer userId) {
        RoleApply roleApply = new RoleApply();
        roleApply.setUserId(userId);
        return roleApply;

    }
}
