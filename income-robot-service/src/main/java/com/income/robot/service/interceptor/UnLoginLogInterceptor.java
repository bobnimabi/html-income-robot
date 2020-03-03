package com.income.robot.service.interceptor;


import com.bbin.utils.project.RequestUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
public class UnLoginLogInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("IP:{},URL:{}", RequestUtils.getIpAddress(request), request.getRequestURI());
        return true;
    }
}
