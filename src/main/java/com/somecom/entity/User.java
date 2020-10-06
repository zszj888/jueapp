package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString(callSuper = true)
@Entity
@Table(name = "t_user")
public class User extends BaseEntity {
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
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<FavoriteTask> favoriteTasks;
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<FavoriteRole> favoriteRoles;
    @ManyToMany
    @JoinTable(name = "user_favor_task", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"))
    private Set<Task> favorTasks;
    @ManyToMany
    @JoinTable(name = "user_favor_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ID"))
    private Set<Role> favorRoles;

    public User() {
    }

    public User(String openid) {
        this.openid = openid;
    }

}