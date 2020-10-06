package com.somecom.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "account_detail")
public class AccountDetails extends BaseEntity {


    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

}
