package com.leslie.controller;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie<br/>
 * 文件名：	RobotController.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.service.impl.RobotMgr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("robot")
@Slf4j
public class RobotController {
    @Autowired
    private RobotMgr robotMgr;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping()
    public void msg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (robotMgr.check(request)) {
            response.getWriter().print(request.getParameter("echostr"));
        }
        String weixinid = "gh_55c5bb057e4a";
        Map<String, String> map = robotMgr.strToMap(robotMgr.getValueFromReq(request));
        String msgType = map.get("MsgType") + "";
        String fromUserName = map.get("FromUserName") + "";
        String currentTime = System.currentTimeMillis() + "";
        String result = "[愉快]终于等到你，还好我没放弃~";
        log.info("robot收到消息:{}", objectMapper.writeValueAsString(map));
        if (msgType.equals("event")) {
            String event = map.get("Event") + "";
            String eventKey = map.get("EventKey") + "";
            if (event.equals(("subscribe"))) {
                //
            }
        } else {
            String content = (map.get("Content") + "").toLowerCase();
            String name = "小主";
            if (content.contains("天气") || content.contains("雨")) {
                String city = robotMgr.getCityFromContent(content);
                log.info("准备查询name:{}的天气", city);
                if (content.contains("未来") || content.contains("明天") || content.contains("后天")) {
                    result = robotMgr.getRecentDesc(city, name);
                } else {
                    result = robotMgr.getRealDesc(city, name);
                }
            } else if (content.contains("你是谁") || content.contains("你叫什么") || content.contains("你是")) {
                result = "伦家是霸天,是主人开发出的一枚智能机器人，不过人家还小呢";
            } else if (content.contains("男") || content.contains("性别")) {
                result = "伦家是一个真正男子汉[悠闲]";
            } else if (content.contains("你喜欢")) {
                result = "我当然喜欢你啦，你这么可爱，这么美丽，让人怎能不心疼[拥抱]";
            } else {
                result = restTemplate.getForObject(
                        "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + java.net.URLEncoder.encode(content, "utf-8"), String.class);
                Map resMa = new ObjectMapper().readValue(result, new TypeReference<Map>() {
                });
                result = resMa.get("content") + "";
            }
        }
        String re = robotMgr.sendText(fromUserName, weixinid, currentTime, result);
        log.info("robot返回消息:{}", re);
        response.getWriter().print(re);
    }
}
