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


@Component("sex")
@Slf4j
public class SexServiceImpl implements RobotService {
    @Autowired
    private RobotMgr robotMgr;

    @Override
    public String response(String content) {
        return "伦家是一个真正男子汉[悠闲]";
    }
}
