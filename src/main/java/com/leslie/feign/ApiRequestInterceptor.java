package com.leslie.feign;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;



import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author yanghaixiao
 */
@Configuration
public class ApiRequestInterceptor implements RequestInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(ApiRequestInterceptor.class);

  @Override
  public void apply(RequestTemplate template) {
    Map<String, Collection<String>> headers = new HashMap<>();
    for (String key : template.headers().keySet()) {
      headers.put(key, template.headers().get(key));
    }

    if (StringUtils.isBlank(MDC.get("trace_id"))) {
      MDC.put("trace_id", UUID.randomUUID().toString().replace("-", ""));
    }

    headers.put("trace_id", Arrays.asList(MDC.get("trace_id")));

    StringBuffer sb = new StringBuffer();
    sb.append("\r\n===========================================================================\r\n");
    sb.append("inbound Message: " + "\r\n");
    sb.append("traceid: ").append(MDC.get("trace_id")).append("\r\n");
    sb.append("url: ").append(template.request().url()).append("\r\n");
    sb.append("querys: ").append(template.queryLine()).append("\r\n");
    sb.append("charset: ").append(template.charset()).append("\r\n");
    sb.append("method: ").append(template.method()).append("\r\n");
    sb.append("headers: ").append(getHeaders(headers)).append("\r\n");
    sb.append("===========================================================================");

    logger.info(sb.toString());

    template.headers(headers);
  }

  private String getHeaders(Map<String, Collection<String>> headers) {
    StringBuilder sb = new StringBuilder();

    for (Entry<String, Collection<String>> header : headers.entrySet()) {
      sb.append(header.getKey()).append(":").append(header.getValue()).append(" | ");
    }

    return sb.toString();
  }

}
