package com.leslie.service.impl;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	WeatherServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.leslie.common.PropMgr;
import com.leslie.remote.BaiduWeatherClient;
import com.leslie.remote.BaiduWeatherResponse;
import com.leslie.service.api.RobotService;

import lombok.extern.slf4j.Slf4j;

@Component("baidu_weather")
@Slf4j
public class BaiduWeatherServiceImpl implements RobotService {
  @Autowired
  private PropMgr propMgr;
  @Autowired
  private BaiduWeatherClient baiduWeatherClient;
  @Value("${weather.baidu.key}")
  private String ak;

  @Override
  public String response(String content) {
    String name = "大宝贝";
    String result = "";
    String city = propMgr.getCityFromContent(content);
    log.info("准备查询name:{}的天气", city);
    BaiduWeatherResponse weatherResponse = baiduWeatherClient.get(city, ak, "json");
    return getRecentDesc(city, name, weatherResponse);
  }

  private String getRecentDesc(String city, String name, BaiduWeatherResponse baiduWeatherResponse) {
    try {
      List<BaiduWeatherResponse.ResultsBean> list = baiduWeatherResponse.getResults();
      StringBuilder result = new StringBuilder();
      for (BaiduWeatherResponse.ResultsBean ss : list) {
        List<BaiduWeatherResponse.ResultsBean.WeatherDataBean> dailys = ss.getWeather_data();
        StringBuilder json = new StringBuilder(String.format("%s, %s未来几日天气情况:\n", name, ss.getCurrentCity()));
        for (BaiduWeatherResponse.ResultsBean.WeatherDataBean daily : dailys) {
          String str = String.format("%s,%s,%s,%s", daily.getDate(), daily.getWeather(), daily.getWind(),
              daily.getTemperature());
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
