package com.leslie.feign;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。
 * <p>
 * 项目名： com.hd123.ras.transfer.h4.feign<br/>
 * 文件名： BizExceptionFeignErrorDecoder.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年05月11日 - yanghaixiao - 创建。<br/>
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BizExceptionFeignErrorDecoder implements feign.codec.ErrorDecoder {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String json = null;
            ResponseErrorMsg responseErrorMsg = null;
            if (response.body() != null) {
                json = new BufferedReader(new InputStreamReader(response.body().asInputStream())).lines().parallel()
                        .collect(Collectors.joining(System.lineSeparator()));
                log.error("访问外部服务<{}>出现错误:<{}>,错误代码:<{}>", response.request().url(), json, response.status());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.status() >= 400 && response.status() <= 499) {
            return new HystrixBadRequestException("http权限问题或资源不存在,错误代码:" + response.status());
        }
        return feign.FeignException.errorStatus(methodKey, response);
    }

    @Setter
    @Getter
    public static class ResponseErrorMsg {
        private String timestamp;
        private int status;
        private String error;
        private String exception;
        private String message;
        private String path;
    }
}
