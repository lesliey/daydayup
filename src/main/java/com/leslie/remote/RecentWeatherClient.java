package com.leslie.remote;

import com.leslie.remote.recent.RecentWeather;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.remote<br/>
 * 文件名：	RecentWeatherClient.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年07月13日 - yanghaixiao - 创建。<br/>
 */

@FeignClient(name = "recentweather", url = "https://free-api.heweather.com/v5")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface RecentWeatherClient {
    /**
     * @param cityName name
     * @param key      ce896f3a8bed406eb416f0ae77bbd259
     * @return
     */
    @RequestMapping(value = "forecast", method = RequestMethod.GET)
    RecentWeather get(@RequestParam("city") String cityName, @RequestParam("key") String key);
}
