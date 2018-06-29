package com.leslie.controller;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie<br/>
 * 文件名：	RobotController.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leslie.common.RobotMgr;
import com.leslie.service.api.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext context;

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
        log.info("robot收到消息:{}", objectMapper.writeValueAsString(map));
        if (msgType.equals("event")) {
            String event = map.get("Event") + "";
            String eventKey = map.get("EventKey") + "";
            if (event.equals(("subscribe"))) {
                // TODO 处理事件
            }
        } else {
            String content = (map.get("Content") + "").toLowerCase();
            RobotService service = robotMgr.getService(context, content);
            String result = service.response(content);
            String re = robotMgr.createWeixinMsg(fromUserName, weixinid, currentTime, result);
            log.info("robot返回消息:{}", re);
            response.getWriter().print(re);
        }
    }
}
