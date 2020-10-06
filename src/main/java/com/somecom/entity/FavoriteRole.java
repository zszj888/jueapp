package com.somecom.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_favor_role")
public class FavoriteRole extends BaseEntity {
    @Column(name = "classify_name")
    private String classifyName;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "role_id")
    private Integer roleId;
}
