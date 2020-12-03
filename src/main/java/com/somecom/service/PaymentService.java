package com.somecom.service;

import com.somecom.entity.PayOrder;
import com.somecom.enums.PayOrderStatusEnum;
import com.somecom.model.ResultVo;
import com.somecom.repo.PayOrderRepository;
import com.somecom.util.PayUtil;
import com.somecom.util.WechatPayParam;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import static com.somecom.util.WechatPayParam.APPID;
import static com.somecom.util.WechatPayParam.KEY;
import static com.somecom.util.WechatPayParam.MCHID;
import static com.somecom.util.WechatPayParam.NOTIFY_URL;
import static com.somecom.util.WechatPayParam.WXQUERY;
import static com.somecom.util.WechatPayParam.WXURL;

@Service
public class PaymentService {

    @Resource
    private PayOrderRepository payOrderRepository;

    //调试模式_统一下单接口 返回XML数据：
//    <xml>
//        <return_code><![CDATA[SUCCESS]]></return_code>
//        <return_msg><![CDATA[OK]]></return_msg>
//        <appid><![CDATA[wx61089dda2b3d8b09]]></appid>
//        <mch_id><![CDATA[1485321192]]></mch_id>
//        <nonce_str><![CDATA[knRaTLkubNv7DuPB]]></nonce_str>
//        <sign><![CDATA[14AB33EFEDCCE51FAE1FCB3FFFB71BDF]]></sign>
//        <result_code><![CDATA[SUCCESS]]></result_code>
//        <prepay_id><![CDATA[wx14130655883686593067e915f9a5d90000]]></prepay_id>
//        <trade_type><![CDATA[JSAPI]]></trade_type>
//    </xml>
    public static void main(String[] args) {
        WechatPayParam wechatPayParam = new WechatPayParam();
        wechatPayParam.setBody("角er小程序-角色征用");
        wechatPayParam.setFee(BigDecimal.valueOf(0.01));
        wechatPayParam.setOpenId("obKZI4zPNr-5utqJEC-ojfCOoQas");
        String s = System.currentTimeMillis() + UUID.randomUUID().toString();
        wechatPayParam.setOrderId(PayUtil.md5(s));
        new PaymentService().pay(wechatPayParam);
    }

    public ResultVo query(String outTradeNo) {
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new TreeMap<>();
        String nonceStr = getRandomStringByLength(32);
        packageParams.put("appid", APPID);
        packageParams.put("mch_id", MCHID);
        packageParams.put("nonce_str", nonceStr);
        packageParams.put("out_trade_no", outTradeNo);//商户订单号

        String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

        //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = PayUtil.sign(prestr, KEY, "utf-8").toUpperCase();

        String xml = "<xml>" +
                "<appid>" + APPID + "</appid>" +
                "<mch_id>" + MCHID + "</mch_id>" +
                "<nonce_str>" + nonceStr + "</nonce_str>" +
                "<out_trade_no>" + outTradeNo + "</out_trade_no>" +
                //怎么保证查询结果正确查到
                "<sign>" + mysign + "</sign>" +
                "</xml>";

        String res = PayUtil.httpRequest(WXQUERY, "POST", xml);
        return ResultVo.ok(res);
    }

    //调试模式_统一下单接口 请求XML数据：
    // <xml><appid>wx61089dda2b3d8b09</appid><body><![CDATA[角er小程序-角色征用]]></body><mch_id>1485321192</mch_id><nonce_str>pr8k7stax42jzv738czpv5cm4fpyhvxo</nonce_str><notify_url>http://www.13cyw.com:8888/pay/notify</notify_url><openid>obKZI4zPNr-5utqJEC-ojfCOoQas</openid><out_trade_no>35CF039A0D82CBA633F9DE37EDA80D3E</out_trade_no><spbill_create_ip>192.168.1.104</spbill_create_ip><total_fee>1</total_fee><trade_type>JSAPI</trade_type><sign>DFEF77915DA43454961FFB795C5C0320</sign></xml>

    public ResultVo pay(WechatPayParam wechatPayParam) {
        Map<String, Object> stringObjectMap = prePay(wechatPayParam);

        if (!"SUCCESS".equals(stringObjectMap.get("ret"))) {
            System.err.println("统一下单结果失败，" + stringObjectMap);
            return ResultVo.err(stringObjectMap);
        }
        return ResultVo.ok(stringObjectMap);
    }

    private String outIp() {
        Socket socket = new Socket();
        String ip;
        try {
            socket.connect(new InetSocketAddress("qq.com", 80));
        } catch (IOException e) {
            ip = "127.0.0.1";
            e.printStackTrace();
        }
        ip = socket.getLocalAddress().toString().substring(1);
        System.out.println(ip);
        return ip;
    }

    /**
     * 获取小程序前端所需要的4个参数
     *
     * @param wechatPayParam
     * @return
     */
    private Map<String, Object> prePay(WechatPayParam wechatPayParam) {
        Map<String, Object> result = new HashMap<>();

        String fee = wechatPayParam.getFee().multiply(BigDecimal.valueOf(100)).intValue() + "";

        //生成的随机字符串
        String nonce_str = getRandomStringByLength(32);
        String outIp = outIp();
        //组装参数，用户生成统一下单接口的签名
        Map<String, String> packageParams = new TreeMap<>();
        packageParams.put("appid", APPID);
        packageParams.put("mch_id", MCHID);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", wechatPayParam.getBody());//商品名称
        packageParams.put("out_trade_no", wechatPayParam.getOrderId());//商户订单号
        packageParams.put("total_fee", fee);//支付金额，这边需要转成字符串类型，否则后面的签名会失败  ObjectTranslate.getString(order.get("transport"))
        packageParams.put("spbill_create_ip", outIp);
        packageParams.put("notify_url", NOTIFY_URL);//支付成功后的回调地址
        packageParams.put("trade_type", "JSAPI");//支付方式
        packageParams.put("openid", wechatPayParam.getOpenId());

        String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

        //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = PayUtil.sign(prestr, KEY, "utf-8").toUpperCase();

        //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
        String xml = "<xml>" + "<appid>" + APPID + "</appid>"
                + "<body><![CDATA[" + wechatPayParam.getBody() + "]]></body>"
                + "<mch_id>" + MCHID + "</mch_id>"
                + "<nonce_str>" + nonce_str + "</nonce_str>"
                + "<notify_url>" + NOTIFY_URL + "</notify_url>"
                + "<openid>" + wechatPayParam.getOpenId() + "</openid>"
                + "<out_trade_no>" + wechatPayParam.getOrderId() + "</out_trade_no>"
                + "<spbill_create_ip>" + outIp + "</spbill_create_ip>"
                + "<total_fee>" + fee + "</total_fee>"
                + "<trade_type>" + "JSAPI" + "</trade_type>"
                + "<sign>" + mysign + "</sign>"
                + "</xml>";

        System.out.println("调试模式_统一下单接口 请求XML数据：" + xml);

//        System.out.println("checkSign>"+PayUtil.checkSign(xml,KEY));
//return null;
        //调用统一下单接口，并接受返回的结果
        String res = PayUtil.httpRequest(WXURL, "POST", xml);

        System.out.println("调试模式_统一下单接口 返回XML数据：" + res);

        // 将解析结果存储在HashMap中
        Map map;
        try {
            map = PayUtil.fromXml(res);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("ret", "pay_fail");
            return result;
        }

        String return_code = (String) map.get("return_code");//返回状态码

        if (return_code.equals("SUCCESS")) {
            String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
            result.put("package", "prepay_id=" + prepay_id);
            long timeStamp = System.currentTimeMillis() / 1000;
            result.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //拼接签名需要的参数
            String stringSignTemp = "appId=" + APPID + "&nonceStr=" + map.get("nonce_str") + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            System.out.println("originString>" + stringSignTemp);
            String paySign = PayUtil.sign(stringSignTemp, KEY, "utf-8").toUpperCase();
            System.out.println("paySign>" + paySign);

            result.put("WxNonceStr", map.get("nonce_str"));
            result.put("package", "prepay_id=" + prepay_id);
            result.put("timeStamp", timeStamp + "");
            result.put("paySign", paySign);
            result.put("ret", "SUCCESS");
        }
        return result;
    }

    /**
     * @param request 请求request
     * @param key     key
     * @return
     * @throws Exception
     */
    public void checkNotifyXml(HttpServletRequest request, HttpServletResponse response, String key) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                assert br != null;
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        // sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);

        Map map = null;
        try {
            map = PayUtil.fromXml(notityXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String return_code = map.get("return_code").toString().toUpperCase();

        if (return_code.equals("SUCCESS") && "SUCCESS".equals(map.get("result_code").toString().toUpperCase())) {
            //进行签名验证，看是否是从微信发送过来的，防止资金被盗
            // 验证签名是否正确
            if (PayUtil.checkSign(notityXml, key)) {
                PayOrder example = new PayOrder();
                example.setOutTradeNo(String.valueOf(map.get("out_trade_no")));

                Optional<PayOrder> one = payOrderRepository.findOne(Example.of(example));
                one.orElseThrow(() -> new RuntimeException("回调支付订单不存在:" + notityXml));
                PayOrder order = one.get();
                order.setBankType(String.valueOf(map.get("bank_type")));
                order.setTotalFee(String.valueOf(map.get("total_fee")));
                order.setTransactionId(String.valueOf(map.get("transaction_id")));
                order.setOutTradeNo(String.valueOf(map.get("out_trade_no")));
                order.setTimeEnd(String.valueOf(map.get("time_end")));
                order.setNotifyMessage(notityXml);
                order.setStatus(PayOrderStatusEnum.SUCCESS.getCode());
                payOrderRepository.saveAndFlush(order);
            }

        }
        resXml = "<xml>" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>" +
                "  <return_msg><![CDATA[OK]]></return_msg>" +
                "</xml>";
        try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            out.write(resXml.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成随机数
     *
     * @param length
     * @return
     */
    private String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
