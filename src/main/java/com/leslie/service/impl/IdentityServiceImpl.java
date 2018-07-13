package com.leslie.service.impl;
/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.service.impl<br/>
 * 文件名：	IdentityServiceImpl.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月29日 - yanghaixiao - 创建。<br/>
 */

import com.leslie.service.api.RobotService;
import org.springframework.stereotype.Component;


@Component("identity")
public class IdentityServiceImpl implements RobotService {
    @Override
    public String response(String content) {
        return "伦家是霸天,是主人开发出的一枚智能机器人，不过人家还小呢";
    }
}
