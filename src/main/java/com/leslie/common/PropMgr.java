package com.leslie.common;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.common<br/>
 * 文件名：	PropMgr.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropMgr {
  public static final List<Map> CITY_MAP_LIST = new ArrayList<>();
  public static final Map<String, String> SMART_HANDLER_MAP = new LinkedHashMap<>();

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  public void readCity() throws IOException {
    InputStream stream = getClass().getResourceAsStream("/city.json");
    String json = stream2String(stream);
    stream.close();
    CITY_MAP_LIST.addAll(objectMapper.readValue(json, new TypeReference<List<Map>>() {
    }));
    InputStream robotStream = getClass().getResourceAsStream("/robot.json");
    json = stream2String(robotStream);
    robotStream.close();
    SMART_HANDLER_MAP.putAll(objectMapper.readValue(json, new TypeReference<LinkedHashMap<String, String>>() {
    }));
    log.info("程序启动中，共读取{}个城市信息,{}个匹配规则", CITY_MAP_LIST.size(), SMART_HANDLER_MAP.size());
  }

  public String getCityCode(String tj) {
    for (Map map : CITY_MAP_LIST) {
      String cityName = map.get("name_cn") + "";
      String namePinyin = map.get("name_pinyin") + "";
      String code = map.get("code") + "";
      if (tj.equals(cityName) || tj.equals(namePinyin)) {
        return code;
      }
    }
    return "-1";
  }

  @Autowired
  private AccessKey accessKey;

  public String getCityFromContent(String content) {
    for (Map map : CITY_MAP_LIST) {
      String city = map.get("name_cn") + "";
      String name_pinyin = map.get("name_pinyin") + "";
      if (content.contains(city) || content.contains(name_pinyin)) {
        return city;
      }
    }
    if (Constants.WIFE_OPENID.equals(accessKey.getFromUserName())) {
      return "苏州";
    }
    return "上海";
  }

  public String stream2String(InputStream is) throws IOException {
    return StreamUtils.copyToString(is, Charset.forName("UTF-8"));
  }
}
