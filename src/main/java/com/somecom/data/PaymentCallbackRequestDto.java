package com.somecom.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentCallbackRequestDto implements Serializable {
    private String mchno;//商户号
    private String tid;//订单分配的手机编号
    private String obid;//开发者传入的 obid 参数
    private String bid;//收钱宝平台订单唯一标识
    private String amount;//订单价格
    private String inamt;//实际支付的金额
    private String paytype;//支付方式
    private String status;// 状态 0，成功
    private String sign;//签名,参照[下单签名算法
}
