package com.leslie.service.impl;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	WeatherServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.common.PropMgr;
import com.leslie.service.api.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;


@Component("weather")
@Slf4j
public class WeatherServiceImpl implements RobotService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PropMgr propMgr;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String response(String content) {
        String name = "小主";
        String result = "";
        String city = propMgr.getCityFromContent(content);
        log.info("准备查询name:{}的天气", city);
        if (content.contains("未来") || content.contains("明天") || content.contains("后天")) {
            result = getRecentDesc(city, name);
        } else {
            result = getRealDesc(city, name);
        }
        return result;
    }

    public String getWeatherByCode(String code) {
        String json = restTemplate.getForObject(String.format("http://www.weather.com.cn/data/sk/%s.html", code), String.class);
        return json;
    }

    public String getRealWeather(String name) {
        String code = propMgr.getCityCode(name);
        return getWeatherByCode(code);
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
            real = String.format("%s，%s，%s当前的天气:%s°C,%s,湿度%s,%s", name, half, realMap.get("city"), realMap.get("temp"), realMap.get("WD") + "" + realMap.get("WS"),
                    realMap.get("SD"), (realMap.get("rain") + "").equals("0") ? "无雨。" : "有雨(上班记得带伞啦)。");
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
                StringBuilder json = new StringBuilder(String.format("%s, %s未来几日天气情况:\n", name, basic.get("city")));
                for (Map daily : daily_forecast) {
                    Map cond = (Map) daily.get("cond");
                    Map tmp = (Map) daily.get("tmp");
                    Map wind = (Map) daily.get("wind");
                    String str = String.format("%s,%s,%s°C-%s°C,%s", (daily.get("date") + "").substring(5, 10), cond.get("txt_d"), tmp.get("min"), tmp.get("max"),
                            wind.get("dir") + "" + wind.get("spd") + "级");
                    json.append(str).append("\n");
                }
                result += json;
            }
            return result;
        } catch (Exception e) {

        }
        return "伦家才疏学浅，找不到你说的地方哎[委屈]~换个近点的试试？";
    }

}
