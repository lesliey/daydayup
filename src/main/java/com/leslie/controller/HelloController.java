package com.leslie.controller;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie<br/>
 * 文件名：	controller.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */

import com.leslie.common.WeixinMgr;
import com.leslie.service.api.weixin.WxUser;
import com.leslie.service.impl.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class HelloController {
    @Autowired
    private WeixinMgr weixinMgr;
    @Autowired
    private WeatherServiceImpl weatherService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public WxUser index() throws IOException {
        return weixinMgr.getUserInfo("oZyK-1QfcdvtDaV0ZNmisDvkh-LQ");
    }


    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    public String msg(@RequestParam("content") String content) {
        return weatherService.response(content);
    }
}
