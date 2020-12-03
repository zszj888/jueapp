package com.somecom.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentRequestDto implements Serializable {
    private String mchno;//必填，商户号，在收钱宝注册后，自动分配
    private String name;//必填，订单销售商品名称（需要进行URL编码）
    private String paytype;//必填，支付方式（1:微信，2:支付宝）
    private String amount;//必填，订单金额
    private String obid;//必填，订单号
    private String ouid;//必填，会话ID，可以空，建议填写开发者平台付款会员ID可以重复利用保护期内相同金额的收款码资源
    private String input;//必填，金额输入方（1：用户输入金额，2：商家指定金额）
    private String offamount;//必填，当用户输入金额，可能需要金额唯一性适配范围： 0不进行匹配，0.1 代表自动在amount～amount+0.1间适配;private0.1 代表amountprivate0.1～amount间适配。
    private String calltype;//调用API的方式 （1：调用收钱宝的支付显示页面，2：返回JSON用于自定义支付页面）
    private String url;//必填，支付成功后前台跳转地址（需要进行URL编码），当calltype=1时不能为空
    private String time;//必填，当前时间戳
    private String sign;//必填，签名
}
