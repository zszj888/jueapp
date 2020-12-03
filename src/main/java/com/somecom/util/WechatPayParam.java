package com.somecom.util;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Project Name: ms
 * File Name: WechatPayParam
 * Package Name: ms.pay.smallpro
 * Date: 2018/5/11  11:47
 * Copyright (c) 2018, tianyul All Rights Reserved.
 * 小程序支付请求参数
 */
@Data
public class WechatPayParam {
    public static final String WXURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String MCHID = "1485321192";
    public static final String APPID = "wx61089dda2b3d8b09";
    public static final String WXQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String KEY = "5BDF0161E4BBB00A6A4DCA4631CB84E3";
    public static final String NOTIFY_URL = "http://www.13cyw.com:8888/pay/notify";

    private String openId;//小程序用户id
    private String orderId;//订单id
    private BigDecimal fee;//总金额  元
    private String body;//商品名称
}
