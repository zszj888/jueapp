package com.somecom.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenpei
 */
public class XmlUtil {

    static Map<String, String> xmlMap = new HashMap<String, String>();

    @SuppressWarnings("unchecked")
    static Map<String, String> fromXML(String xmlStr) {
        XStream magicApi = new XStream();
        XStream.setupDefaultSecurity(magicApi);
        magicApi.allowTypes(new Class[]{Map.class});
        magicApi.registerConverter(new MapEntryConverter());
        magicApi.alias("xml", Map.class);
        return (Map<String, String>) magicApi.fromXML(xmlStr);
    }

    public static void main(String[] args) {
        String xml = "<xml><appid><![CDATA[wx61089dda2b3d8b09]]></appid><bank_type><![CDATA[CMB_CREDIT]]></bank_type><cash_fee><![CDATA[2]]></cash_fype><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1485321192]]></mch_id><nonce_str><![CDATA[jrbqkucff91qom47bgoljy5eggg739yy]]></nonce_str><openid><![CDATA[obKZI4zPNr-5utqJEC-ojfCOoQas]]></openid><out_trade_no><![CDATA[640B529ACBC7CDA286FE660D601B09D5]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[BB0759E02A110E80C61F3BBEB49F8483]]></sign><time_end><![CDATA[20201115090100]]></time_end><total_fee>2</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000742202011155368273174]]></transaction_id></xml>";
//
        XStream magicApi = new XStream();
        XStream.setupDefaultSecurity(magicApi);
        magicApi.allowTypes(new Class[]{Map.class});
        magicApi.registerConverter(new MapEntryConverter());
        magicApi.alias("xml", Map.class);

        Map<String, String> extractedMap = (Map<String, String>) magicApi.fromXML(xml);
        System.out.println(extractedMap);
    }

    /**
     * xml字符串转换成Map
     * 获取标签内属性值和text值
     *
     * @param xml
     * @return
     * @throws Exception
     */
    @Deprecated
    public static Map<String, String> xmlToMap(String xml) throws Exception {
        StringReader reader = new StringReader(xml);
        InputSource source = new InputSource(reader);
        SAXReader sax = new SAXReader(); // 创建一个SAXReader对象
        Document document = sax.read(source); // 获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root = document.getRootElement(); // 获取根节点
        Map<String, String> map = XmlUtil.getNodes(root); // 从根节点开始遍历所有节点
        return map;
    }

    /**
     * 从指定节点开始,递归遍历所有子节点
     *
     * @author chenleixing
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getNodes(Element node) {
        xmlMap.put(node.getName().toLowerCase(), node.getTextTrim());
        List<Attribute> listAttr = node.attributes(); // 当前节点的所有属性的list
        for (Attribute attr : listAttr) { // 遍历当前节点的所有属性
            String name = attr.getName(); // 属性名称
            String value = attr.getValue(); // 属性的值
            xmlMap.put(name, value.trim());
        }

        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements(); // 所有一级子节点的list
        for (Element e : listElement) { // 遍历所有一级子节点
            XmlUtil.getNodes(e); // 递归
        }
        return xmlMap;

    }

    public static class MapEntryConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return AbstractMap.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

            AbstractMap map = (AbstractMap) value;
            for (Object obj : map.entrySet()) {
                Map.Entry entry = (Map.Entry) obj;
                writer.startNode(entry.getKey().toString());
                Object val = entry.getValue();
                if (null != val) {
                    writer.setValue(val.toString());
                }
                writer.endNode();
            }

        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

            Map<String, String> map = new HashMap<>();

            while (reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                map.put(key, value);

                reader.moveUp();
            }

            return map;
        }

    }
}