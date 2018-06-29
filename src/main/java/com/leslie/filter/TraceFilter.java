package com.leslie.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component("xiuTraceFilter")
public class TraceFilter implements Filter {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = request.getHeader("trace_id");
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put("trace_id", traceId);
        Map<String, String> params = new HashMap<>();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            params.put(paraName, request.getParameter(paraName));
        }
        log.info("访问路径:{},请求参数:{},客户端:{}", request.getPathInfo(), objectMapper.writeValueAsString(params), request.getRemoteHost());
        filterChain.doFilter(getRequestBody(request), servletResponse);
    }

    @Override
    public void destroy() {

    }

    private HttpServletRequest getRequestBody(HttpServletRequest request) throws IOException {

        HttpServletRequest request1 = new BodyReaderHttpServletRequestWrapper(request);
        return request1;
    }
}