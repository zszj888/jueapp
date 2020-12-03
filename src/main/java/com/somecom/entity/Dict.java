package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.StatusUtil;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
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
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "sys_dict")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
public class Dict implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private Byte type;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String value;
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
}
