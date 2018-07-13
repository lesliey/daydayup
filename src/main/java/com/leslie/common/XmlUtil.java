package com.leslie.common;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.common<br/>
 * 文件名：	XmlUtil.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */


import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XmlUtil {

    public static Map<String, String> parse(String xml) {
        Map<String, String> map = new HashMap<>();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            for (Object obj : elements) {
                Element element = (Element) obj;
                map.put(element.getName(), element.getTextTrim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        String xml = "<xml> \n" +
                "<ToUserName>![CDATA[toUser]]</ToUserName> \n" +
                "<FromUserName>![CDATA[fromUser]]</FromUserName> \n" +
                "<CreateTime>1348831860</CreateTime> \n" +
                "<MsgType>![CDATA[image] ]</MsgType> \n" +
                "<PicUrl>![CDATA[this is a url]]</PicUrl> <MediaId>![CDATA[media_id]]</MediaId> <MsgId>1234567890123456</MsgId> </xml>";
        parse(xml);
    }
}
