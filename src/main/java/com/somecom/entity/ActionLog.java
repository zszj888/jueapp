package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 小懒虫
 * @date 2018/10/19
 */
@Data
@Entity
@Table(name = "sys_action_log")
@EntityListeners(AuditingEntityListener.class)
public class ActionLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Byte type;
    private String ipaddr;
    private String clazz;
    private String method;
    private String model;
    private Long recordId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;
    @CreatedDate
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "oper_by")
    @JsonIgnore
    private SysUser operBy;
    private String operName;

    public ActionLog() {
    }

    /**
     * 封装日志对象
     *
     * @param name    日志名称
     * @param message 日志消息
     */
    public ActionLog(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
