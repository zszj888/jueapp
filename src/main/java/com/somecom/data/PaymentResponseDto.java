package com.somecom.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentResponseDto implements Serializable {
    private String status;//请求结果（只有为 ok 的时候才有其他的值，注意ok不是交易成功标志）
    private String mchno;//商户号
    private String tid;//该订单分配到的手机编号
    private String obid;//开发者平台订单号，
    private String bid;//收钱宝平台订单唯一标示可用于查询订单状态
    private String paytype;//支付类型 1:微信 2：支付宝
    private String amount;//交易金额
    private String inamt;//实际支付金额
    private String expires;//支付二维码剩余有效秒数，-1：已过期
    private String qrcode;//支付二维码
    private String sign;// 签名
}
