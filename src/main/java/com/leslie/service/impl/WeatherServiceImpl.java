package com.leslie.service.impl;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	WeatherServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import com.leslie.common.PropMgr;
import com.leslie.remote.RecentWeatherClient;
import com.leslie.remote.TodayWeather;
import com.leslie.remote.TodayWeatherClient;
import com.leslie.remote.TodayWeatherInfo;
import com.leslie.remote.recent.DailyForecast;
import com.leslie.remote.recent.HeWeather5;
import com.leslie.remote.recent.RecentWeather;
import com.leslie.service.api.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.GregorianCalendar;
import java.util.List;


@Component("weather")
@Slf4j
public class WeatherServiceImpl implements RobotService {
    @Autowired
    private PropMgr propMgr;
    @Autowired
    private TodayWeatherClient todayWeatherClient;
    @Autowired
    private RecentWeatherClient recentWeatherClient;
    @Value("${weather.recent.key}")
    private String recentWeatherKey;

    @Override
    public String response(String content) {
        String name = "大宝贝";
        String result = "";
        String city = propMgr.getCityFromContent(content);
        log.info("准备查询name:{}的天气", city);
        if (content.contains("未来") || content.contains("明天") || content.contains("后天")) {
            result = getRecentDesc(city, name);
        } else {
            try {
                String code = propMgr.getCityCode(city);
                TodayWeather t = todayWeatherClient.get(code);
                TodayWeatherInfo weatherInfo = t.getWeatherinfo();
                GregorianCalendar ca = new GregorianCalendar();
                String half = ca.get(GregorianCalendar.AM_PM) == 0 ? "上午好" : "下午好";
                return String.format("%s，%s，%s当前的天气:%s°C,%s,湿度%s,%s", name, half, weatherInfo.getCity(), weatherInfo.getTemp(), weatherInfo.getWD() + "" + weatherInfo.getWS(),
                        weatherInfo.getWD(), "0".equals(weatherInfo.getRain()) ? "无雨。" : "有雨(上班记得带伞啦)。");
            } catch (Exception e) {
                log.error("今日天气查询失败", e);
                return "伦家才疏学浅，找不到你说的地方哎[委屈]~换个近点的试试？";
            }
        }
        return result;
    }


    public String getRecentDesc(String city, String name) {
        try {
            RecentWeather recentWeather = recentWeatherClient.get(city, recentWeatherKey);
            List<HeWeather5> list = recentWeather.getHeWeather5();
            StringBuilder result = new StringBuilder();
            for (HeWeather5 ss : list) {
                List<DailyForecast> dailys = ss.getDailyForecast();
                StringBuilder json = new StringBuilder(String.format("%s, %s未来几日天气情况:\n", name, ss.getBasic().getCity()));
                for (DailyForecast daily : dailys) {
                    String str = String.format("%s,%s,%s°C-%s°C,%s", (daily.getDate()).substring(5, 10),
                            daily.getCond().getTxtD(), daily.getTmp().getMin(), daily.getTmp().getMax(),
                            daily.getWind().getDir() + "" + daily.getWind().getSpd() + "级");
                    json.append(str).append("\n");
                }
                result.append(json);
            }
            return result.toString();
        } catch (Exception e) {
            log.error("近期天气查询失败", e);
        }
        return "伦家才疏学浅，找不到你说的地方哎[委屈]~换个近点的试试？";
    }

}
