package com.leslie.service.impl;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	WeatherServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import com.leslie.common.RobotMgr;
import com.leslie.service.api.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("weather")
@Slf4j
public class WeatherServiceImpl implements RobotService {
    @Autowired
    private RobotMgr robotMgr;

    @Override
    public String response(String content) {
        String name = "小主";
        String result = "";
        String city = robotMgr.getCityFromContent(content);
        log.info("准备查询name:{}的天气", city);
        if (content.contains("未来") || content.contains("明天") || content.contains("后天")) {
            result = robotMgr.getRecentDesc(city, name);
        } else {
            result = robotMgr.getRealDesc(city, name);
        }
        return result;
    }
}
