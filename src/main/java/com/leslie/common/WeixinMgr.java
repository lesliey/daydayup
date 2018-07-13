package com.leslie.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.service.api.weixin.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;


@Component
public class WeixinMgr {
    @Value("{weixin.robbot.token:wxx}")
    private String token = "wxx";
    private String wxAppId = "wxa094e00092768755";
    private String wxAppSecret = "df83ff6496f22ed1212b895c328fe8a9";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 验证请求来自微信
     */
    public boolean check(HttpServletRequest request) {
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String signature = request.getParameter("signature");
        String[] strs = {
                timestamp, nonce, token};
        Arrays.sort(strs);
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return getSha1(sb.toString()).equals(signature);
    }

    // SHA1加密
    public String getSha1(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] sourceBytes = source.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(sourceBytes);
            byte[] tempBytes = md.digest();
            char[] objectChars = new char[tempBytes.length * 2];
            int i = 0;
            for (byte tempByte : tempBytes) {
                objectChars[i++] = hexDigits[tempByte >>> 4 & 0xf];
                objectChars[i++] = hexDigits[tempByte & 0xf];
            }
            return new String(objectChars);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * xml字符串转map
     *
     * @param str xml
     */
    public Map<String, String> xml2Map(String str) {
        return XmlUtil.parse(str);
    }

    /**
     * 构造图文消息
     *
     * @param toUserName   接收方id
     * @param fromUserName 发送方id
     * @param msgType      消息类型
     * @param title
     * @param description
     * @param picUrl
     */
    public String createImgText(String toUserName, String fromUserName,
                                String msgType, String title, String description, String picUrl) {
        String createTime = System.currentTimeMillis() + "";
        String param = "<xml><ToUserName><![CDATA[" + toUserName + "]]></ToUserName><FromUserName><![CDATA[" + fromUserName
                + "]]></FromUserName><CreateTime>" + createTime + "</CreateTime><MsgType><![CDATA[" + msgType
                + "]]></MsgType><ArticleCount>1</ArticleCount><Articles><item><Title><![CDATA[" + title
                + "]]></Title><Description><![CDATA[" + description + "]]></Description><PicUrl><![CDATA[" + picUrl
                + "]]></PicUrl></item></Articles></xml>";
        return param;
    }

    /**
     * 构造文本消息
     */

    public String createTextMsg(String toUserName, String fromUserName,
                                String content) {
        String currentTime = System.currentTimeMillis() + "";
        return "<xml><ToUserName><![CDATA[" + toUserName + "]]></ToUserName><FromUserName><![CDATA[" + fromUserName
                + "]]></FromUserName><CreateTime>" + currentTime + "</CreateTime><MsgType>text</MsgType><Content><![CDATA["
                + content + "]]></Content></xml>";
    }

    /**
     * 获取微信token
     */
    public String getAccessToken() throws IOException {
        String json = restTemplate.getForObject(String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", wxAppId, wxAppSecret), String.class);
        Map map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        return map.get("access_token") + "";
    }


    /**
     * 个人订阅号，无权限调用..日
     * 获取微信用户基本信息
     */
    @Deprecated
    public WxUser getUserInfo(String openid) throws IOException {
        String wxToken = getAccessToken();
        return restTemplate.getForObject(String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", wxToken, openid), WxUser.class);
    }

}
