package com.leslie.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.UpApplication;
import com.leslie.common.WeixinMgr;
import com.leslie.remote.BaiduWeatherClient;
import com.leslie.remote.TodayWeatherClient;
import com.leslie.service.impl.BaiduWeatherServiceImpl;
import com.leslie.service.impl.WeatherServiceImpl;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名： com.leslie.test<br/>
 * 文件名： WeatherTest.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年07月13日 - yanghaixiao - 创建。<br/>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UpApplication.class, properties = {})
public class WeatherTest {
  @Autowired
  private WeatherServiceImpl w;
  @Autowired
  private TodayWeatherClient client;
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BaiduWeatherClient baiduWeatherClient;
  @Autowired
  private BaiduWeatherServiceImpl baiduWeatherService;
  @Autowired
  private WeixinMgr weixinMgr;

  @Test
  public void testRecent() {
    System.out.println(w.response("上海未来天气"));
  }

  @Test
  public void testToday() throws JsonProcessingException {
    System.out.println(objectMapper.writeValueAsString(client.get("101010600")));
    System.out.println(w.response("吉林天气"));
  }

  @Test
  public void testbaidu() throws JsonProcessingException {
    System.out.println(objectMapper.writeValueAsString(baiduWeatherService.response("上海天气")));
  }

  private String wife = "oZyK-1c-jkgZDOsN01wr-ukvxpYU";

  @Test
  public void testPush() throws IOException {
    weixinMgr.pushMsg("gh_55c5bb057e4a", "asdasd");
  }
}
