package com.income.robot.service.interceptor;


import com.bbin.common.pojo.AuthToken;
import com.bbin.utils.project.RequestUtils;
import com.bbin.utils.project.XcTokenUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
public class LoginLogInterceptor extends HandlerInterceptorAdapter {

  @Value("${spring.profiles.active}")
  private String environment;
  
  @Autowired
  private StringRedisTemplate redis;
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    AuthToken userInfo = XcTokenUtil.getUserInfo(request, this.environment, this.redis);
    log.info("IP:" + RequestUtils.getIpAddress(request) + " + userInfo.getUsername() + " + request.getRequestURI());
    return true;
  }
}
