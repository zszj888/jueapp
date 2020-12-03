package com.somecom.entity;

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
    private Integer id;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

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
