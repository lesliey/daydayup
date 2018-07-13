package com.leslie.service.impl;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	DefaultServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.service.api.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;


@Component("default")
public class DefaultServiceImpl implements RobotService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String response(String content) throws IOException {
        String result = restTemplate.getForObject(
                "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + java.net.URLEncoder.encode(content, "utf-8"), String.class);
        Map resMa = new ObjectMapper().readValue(result, new TypeReference<Map>() {
        });
        return resMa.get("content") + "";
    }
}
