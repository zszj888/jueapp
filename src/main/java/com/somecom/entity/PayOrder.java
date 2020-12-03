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

@ToString(callSuper = true)
@Setter
@Getter
@Entity
@Table(name = "t_pay_order")
public class PayOrder extends BaseEntity {
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Column(name = "open_id")
    private String openId;
    private BigDecimal balance;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "pay_status", columnDefinition = "TINYINT(1) NOT NULL COMMENT '1新建，2成功，3失败'")
    private byte status;
    @Column(name = "business_type", columnDefinition = "TINYINT(1) NOT NULL COMMENT '1角色支付，2任务支付'")
    private byte businessType;
    @Column(name = "business_id", columnDefinition = "INT(11) NOT NULL COMMENT '角色ID或者任务ID'")
    private Integer businessId;
    @Column(name = "body", columnDefinition = "VARCHAR(255) NOT NULL COMMENT '支付描述，例如：中国联通充值中心-话费充值-50元'")
    private String body;
    private BigDecimal fee;
    private BigDecimal handleRate;
    @Column(name = "min_charge_handle_fee")
    private BigDecimal minCharge;
    @Column(name = "pay_time_stamp")
    private String timeStamp;
    @Column(name = "out_trade_no")
    private String outTradeNo;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "total_fee")
    private String totalFee;
    @Column(name = "bank_type")
    private String bankType;
    @Column(name = "notify_message")
    private String notifyMessage;
    @Column(name = "time_end")
    private String timeEnd;

}