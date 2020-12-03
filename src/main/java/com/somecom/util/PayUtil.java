package com.somecom.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @author 作者 刘天宇
 * @version 创建时间：2017年10月31日 上午10:41:35
 * 功能 支付工具类--微信支付
 */
public class PayUtil {


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        System.out.println("key>" + key);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    public static String md5(String args) {
        return DigestUtils.md5Hex(args.getBytes()).toUpperCase();
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    @SuppressWarnings("unused")
    private static boolean isValidChar(char ch) {
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
            return true;
        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
            return true;// 简体中文汉字编码     
        return false;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符     
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }
        return prestr.toString();
    }

    /**
     * @param requestUrl    请求地址
     * @param requestMethod 请求方法
     * @param outputStr     参数
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // 创建SSLContext     
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //往服务器端写内容     
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容     
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     */
    @Deprecated
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map doXMLParse(String strxml) throws Exception {
        if (!StringUtils.hasText(strxml)) {
            return null;
        }
        return XmlUtil.xmlToMap(strxml);
    }


    public static Map<String, String> fromXml(String strxml) {
        if (!StringUtils.hasText(strxml)) {
            return null;
        }
        return XmlUtil.fromXML(strxml);
    }

    /**
     * 验签
     *
     * @param xmlString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean checkSign(String xmlString, String key) {

        Map<String, String> map = fromXml(xmlString);

        String signFromAPIResponse = map.get("sign");

        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名

        map.remove("sign");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较

        String signForAPIResponse = getSign(map, key);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {

            //签名验不过，表示这个API返回的数据有可能已经被篡改了

            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);

            return false;

        }

        System.out.println("恭喜，API返回的数据签名验证通过!!!");

        return true;

    }

    public static String getSign(Map<String, String> map, String key) {
        SortedMap<String, String> signParams = new TreeMap<>();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            signParams.put(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        return createSign("utf-8", signParams, key);
    }

    @SuppressWarnings("rawtypes")
    private static String createSign(String characterEncoding, SortedMap<String, String> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + key);//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        return Md5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    }

}

