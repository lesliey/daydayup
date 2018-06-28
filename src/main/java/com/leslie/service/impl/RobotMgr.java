package com.leslie.service.impl;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名： com.qianfan123.sas<br/>
 * 文件名： RobotMgr.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.common.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RobotMgr {
    public static final List<Map> CITY_MAP_LIST = new ArrayList<>();
    @Value("{weixin.robbot.token:wxx}")
    public static final String token = "wxx";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void readCity() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/city.json");
        String json = new BufferedReader(new InputStreamReader(stream)).lines()
                .parallel()
                .collect(Collectors.joining(System.lineSeparator()));
        CITY_MAP_LIST.addAll(objectMapper.readValue(json, new TypeReference<List<Map>>() {
        }));
    }

    public String getCityCode(String tj) {
        for (Map map : CITY_MAP_LIST) {
            String cityName = map.get("name_cn") + "";
            String namePinyin = map.get("name_pinyin") + "";
            String code = map.get("code") + "";
            if (tj.equals(cityName) || tj.equals(namePinyin)) {
                return code;
            }
        }
        return "-1";
    }

    public String getWeatherByCode(String code) {
        String json = restTemplate.getForObject(String.format("http://www.weather.com.cn/data/sk/%s.html", code), String.class);
        return json;
    }

    public String getRealWeather(String name) {
        return getWeatherByCode(getCityCode(name));
    }

    public String getRecentWeather(String name) {
        try {
            return restTemplate.getForObject("https://free-api.heweather.com/v5/forecast?city=" + name + "&key=ce896f3a8bed406eb416f0ae77bbd259", String.class);
        } catch (Exception e) {

        }
        return null;
    }

    public String getRealDesc(String city, String name) {
        try {
            String real = getRealWeather(city);
            Map realMap = objectMapper.readValue(real, new TypeReference<Map>() {
            });
            realMap = (Map) realMap.get("weatherinfo");
            GregorianCalendar ca = new GregorianCalendar();
            String half = ca.get(GregorianCalendar.AM_PM) == 0 ? "上午好" : "下午好";
            real = String.format("%s，%s，%s当前的天气:%s°C,%s,湿度%s,%s", new Object[]{
                    name, half, realMap.get("city"), realMap.get("temp"), realMap.get("WD") + "" + realMap.get("WS"),
                    realMap.get("SD"), (realMap.get("rain") + "").equals("0") ? "无雨。" : "有雨(上班记得带伞啦)。"});
            return real;

        } catch (Exception e) {

        }
        return "伦家才疏学浅，找不到你说的地方哎[委屈]~换个近点的试试？";
    }

    public String getRecentDesc(String city, String name) {
        try {
            String recent = getRecentWeather(city);
            Map recentMap = objectMapper.readValue(recent, new TypeReference<Map>() {
            });
            List<Map> list = (List<Map>) recentMap.get("HeWeather5");
            String result = "";
            for (Map ss : list) {
                Map basic = (Map) ss.get("basic");
                List<Map> daily_forecast = (List<Map>) ss.get("daily_forecast");
                String json = String.format("%s, %s未来几日天气情况:\n", name, basic.get("city"));
                for (Map daily : daily_forecast) {
                    Map cond = (Map) daily.get("cond");
                    Map tmp = (Map) daily.get("tmp");
                    Map wind = (Map) daily.get("wind");
                    String str = String.format("%s,%s,%s°C-%s°C,%s", new Object[]{
                            (daily.get("date") + "").substring(5, 10), cond.get("txt_d"), tmp.get("min"), tmp.get("max"),
                            wind.get("dir") + "" + wind.get("spd") + "级"});
                    json += str + "\n";
                }
                result += json;
            }
            return result;
        } catch (Exception e) {

        }
        return "伦家才疏学浅，找不到你说的地方哎[委屈]~换个近点的试试？";
    }

    public String getCityFromContent(String content) {
        for (Map map : CITY_MAP_LIST) {
            String city = map.get("name_cn") + "";
            String name_pinyin = map.get("name_pinyin") + "";
            if (content.contains(city) || content.contains(name_pinyin)) {
                return city;
            }
        }
        return "siyang";
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
            for (int j = 0; j < tempBytes.length; j++) {
                objectChars[i++] = hexDigits[tempBytes[j] >>> 4 & 0xf];
                objectChars[i++] = hexDigits[tempBytes[j] & 0xf];
            }
            return new String(objectChars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 验证请求来自微信
    public boolean check(HttpServletRequest request) {
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String signature = request.getParameter("signature");
        String[] strs = {
                timestamp, nonce, token};
        Arrays.sort(strs);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
        }
        return getSha1(sb.toString()).equals(signature);
    }

    // 从请求流中获取值
    public String getValueFromReq(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            byte[] bytes = new byte[1024];
            int length = -1;
            StringBuffer sb = new StringBuffer();
            while ((length = is.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, length));
            }
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    // String转Map
    public Map<String, String> strToMap(String str) {
        return XmlUtil.parse(str);
    }

    // 发送图文消息
    public void sendImgText(HttpServletResponse response, String toUserName, String fromUserName, String createTime,
                            String msgType, String title, String description, String picUrl) {
        String param = "<xml><ToUserName><![CDATA[" + toUserName + "]]></ToUserName><FromUserName><![CDATA[" + fromUserName
                + "]]></FromUserName><CreateTime>" + createTime + "</CreateTime><MsgType><![CDATA[" + msgType
                + "]]></MsgType><ArticleCount>1</ArticleCount><Articles><item><Title><![CDATA[" + title
                + "]]></Title><Description><![CDATA[" + description + "]]></Description><PicUrl><![CDATA[" + picUrl
                + "]]></PicUrl></item></Articles></xml>";
        try {
            response.getWriter().print(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送文本消息
    public String sendText(String toUserName, String fromUserName, String createTime,
                           String content) {
        String param = "<xml><ToUserName><![CDATA[" + toUserName + "]]></ToUserName><FromUserName><![CDATA[" + fromUserName
                + "]]></FromUserName><CreateTime>" + createTime + "</CreateTime><MsgType>text</MsgType><Content><![CDATA["
                + content + "]]></Content></xml>";
        return param;
    }

}
