package com.leslie.remote;

import com.leslie.feign.TodayWeatherConfiguration;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.remote<br/>
 * 文件名：	TodayWeatherClient.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年07月13日 - yanghaixiao - 创建。<br/>
 */

@FeignClient(name = "todayweather", url = "http://www.weather.com.cn/data/sk", configuration = TodayWeatherConfiguration.class)
public interface TodayWeatherClient {
    @RequestMapping(value = "{cityCode}.html", method = RequestMethod.GET)
    TodayWeather get(@PathVariable("cityCode") String cityCode);
}
