package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@ToString(callSuper = true)
@Setter
@Getter
@Table(name = "t_transaction")
public class Transaction extends BaseEntity {
    private BigDecimal amount;
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    @Column(name = "user_id")
    private Integer userId;
    //1:转入 2：转出
    @Column(name = "transaction_type")
    private Byte transType;

}