package com.somecom.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.somecom.model.LocalDateTimeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", updatable = false, nullable = false)
//    @ExcelProperty(index = 0, value = "编号")
    @ExcelIgnore
    private Integer id;

    @Column(name = "last_update_time")
//    @ExcelProperty(converter = LocalDateTimeConverter.class)
    @ExcelIgnore
    private LocalDateTime lastUpdateTime;

    @ExcelIgnore
    @Column(name = "version")
    private Integer version;

    @PrePersist
    public void prePersist() {
        version = 0;
        lastUpdateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdateTime = LocalDateTime.now();
    }
}
