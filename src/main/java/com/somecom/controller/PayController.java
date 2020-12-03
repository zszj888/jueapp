package com.somecom.controller;

import com.somecom.model.ResultVo;
import com.somecom.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.somecom.util.WechatPayParam.KEY;

@RestController
@Api(tags = "支付管理")
@RequestMapping(path = "/pay")
public class PayController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value = "支付成功后给微信回调", notes = "支付成功后给微信回调使用")
    @PostMapping("/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        paymentService.checkNotifyXml(request, response, KEY);
    }

    @ApiOperation(value = "支付结果查询", notes = "支付结果查询")
    @GetMapping("/query")
    public ResultVo queryResult(String outTradeNo) {
        return paymentService.query(outTradeNo);
    }
}