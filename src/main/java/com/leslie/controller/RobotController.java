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
import com.leslie.common.WeixinMgr;
import com.leslie.service.api.RobotService;
import com.leslie.service.api.weixin.BaseWxMsg;
import com.leslie.service.api.weixin.WxEventMsg;
import com.leslie.service.api.weixin.WxTextMsg;
import com.leslie.service.api.weixin.WxVoiceMsg;
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
    private WeixinMgr weixinMgr;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext context;
    private String weixinid = "gh_55c5bb057e4a";

    @RequestMapping
    public void msg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (weixinMgr.check(request)) {
            response.getWriter().print(request.getParameter("echostr"));
        }
        Map<String, String> map = weixinMgr.xml2Map(robotMgr.getValueFromReq(request));
        String body = objectMapper.writeValueAsString(map);
        log.info("robot收到消息:{}", body);
        BaseWxMsg msg = objectMapper.readValue(body, BaseWxMsg.class);
        log.info("robot收到消息类型:{}", msg.getMsgType());
        String msgType = msg.getMsgType();
        String reXml = "";
        switch (msgType) {
            case BaseWxMsg.EVENT_TYPE: {
                WxEventMsg eventMsg = objectMapper.readValue(body, WxEventMsg.class);
                reXml = doEvent(eventMsg);
            }
            break;
            case BaseWxMsg.TEXT_TYPE: {
                WxTextMsg textMsg = objectMapper.readValue(body, WxTextMsg.class);
                reXml = doTextMsg(textMsg);
            }
            break;
            case BaseWxMsg.VOICE_TYPE: {
                WxVoiceMsg voiceMsg = objectMapper.readValue(body, WxVoiceMsg.class);
                reXml = doVoiceMsg(voiceMsg);
            }
            break;
            default: {
                log.info("MsgType:{}暂未适配,稍后推出", msgType);
                reXml = weixinMgr.createTextMsg(msg.getFromUserName(), weixinid, "终于等到你，还好我没放弃;不过MsgType:" + msgType + "暂未适配,稍后推出");
            }
        }
        response.getWriter().print(reXml);
    }

    private String doEvent(WxEventMsg msg) {
        String event = msg.getEvent();
        String eventKey = msg.getEventKey();
        String reXml = "";
        if (event.equals(WxEventMsg.SUBSCRIBE)) {
            log.info("有新人<{}>关注了公众号,发送欢迎信息", msg.getFromUserName());
            reXml = weixinMgr.createTextMsg(msg.getFromUserName(), weixinid, "欢迎关注.");
        }
        if (event.equals(WxEventMsg.UNSUBSCRIBE)) {
            log.info("有人<{}>取关了公众号", msg.getFromUserName());
        }
        return reXml;
    }

    private String doTextMsg(WxTextMsg msg) throws IOException {
        String content = msg.getContent();
        RobotService service = robotMgr.getService(context, content);
        String result = service.response(content);
        return weixinMgr.createTextMsg(msg.getFromUserName(), weixinid, result);
    }

    private String doVoiceMsg(WxVoiceMsg msg) throws IOException {
        String content = msg.getRecognition();
        RobotService service = robotMgr.getService(context, content);
        String result = service.response(content);
        return weixinMgr.createTextMsg(msg.getFromUserName(), weixinid, result);
    }
}
