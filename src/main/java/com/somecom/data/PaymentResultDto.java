package com.somecom.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentResultDto implements Serializable {
    private String status;// status=0 交易成功；status=-1 订单不存在，status=1 正在处理，status=10 交易失败
    private String mchno;//商户号
    private String tid;//该订单分配到的手机编号
    private String obid;//开发者平台订单号，
    private String bid;//收钱宝平台订单唯一标示可用于查询订单状态
    private String paytype;//,支付方式 1:微信 2：支付宝
    private String amount;//交易金额
    private String inamt;//实际支付金额
    private String expires;//支付二维码剩余有效秒数，-1：已过期
    private String qrcode;//支付二维码
    private String sign;//签名
}
