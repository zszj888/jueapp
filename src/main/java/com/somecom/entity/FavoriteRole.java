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
@Table(name = "user_favor_role")
public class FavoriteRole extends BaseEntity {
    @Column(name = "classify_name")
    private String classifyName;
    @Column(name = "user_id")
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public static FavoriteRole ofUser(Integer userId) {
        FavoriteRole favoriteRole = new FavoriteRole();
        favoriteRole.setUserId(userId);
        return favoriteRole;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
