package com.leslie.common;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名： com.qianfan123.sas<br/>
 * 文件名： RobotMgr.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年06月28日 - yanghaixiao - 创建。<br/>
 */

import com.leslie.service.api.RobotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

import static com.leslie.common.PropMgr.SMART_HANDLER_MAP;

@Component
@Slf4j
public class RobotMgr {
    @Autowired
    private PropMgr propMgr;

    public RobotService getService(ApplicationContext context, String content) {
        for (String key : SMART_HANDLER_MAP.keySet()) {
            if (content.contains(key)) {
                log.info("启用处理器:{}", SMART_HANDLER_MAP.get(key));
                return (RobotService) context.getBean(SMART_HANDLER_MAP.get(key));
            }
        }
        log.info("启用默认处理器");
        return (RobotService) context.getBean("default");
    }


    /**
     * 从请求流中获取body
     *
     * @param request 请求
     */
    public String getValueFromReq(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            return propMgr.stream2String(is);
        } catch (Exception e) {
            return "";
        }
    }

}
