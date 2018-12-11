package com.leslie.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.leslie.feign.BaiduConfiguration;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名： com.leslie.remote<br/>
 * 文件名： RecentWeatherClient.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年07月13日 - yanghaixiao - 创建。<br/>
 */

@FeignClient(name = "baiduWeather", url = "http://api.map.baidu.com/telematics/v3/",
    configuration = BaiduConfiguration.class)
@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BaiduWeatherClient {
  // weather?output=json&ak=Gy7SGUigZ4HxGYDaq9azWy09"
  @RequestMapping(value = "weather", method = RequestMethod.GET)
  BaiduWeatherResponse get(@RequestParam("location") String cityName, @RequestParam("ak") String ak,
      @RequestParam("output") String output);
}
