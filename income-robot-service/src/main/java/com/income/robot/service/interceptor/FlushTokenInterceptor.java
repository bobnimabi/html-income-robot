package com.income.robot.service.interceptor;


import com.bbin.common.pojo.AuthToken;
import com.bbin.utils.project.RequestCheck;
import com.bbin.utils.project.XcCookieUtil;
import com.bbin.utils.project.XcTokenUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
@Slf4j
@Service
public class FlushTokenInterceptor extends HandlerInterceptorAdapter {

  @Value("${spring.profiles.active}")
  private String environment;
  
  @Autowired
  private StringRedisTemplate redis;
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (this.environment.equals("prod") || this.environment.equals("test")) {
      String uid = XcCookieUtil.getTokenFormCookie(request);
      AuthToken userToken = XcTokenUtil.getUserToken(uid, this.redis);
      RequestCheck.flushJwtInDate(uid, (RedisTemplate)this.redis);
      RequestCheck.flushLoginFlag(userToken.getUsername(), (RedisTemplate)this.redis);
      log.info("uid:{}", uid);
    }
    return true;
  }
}
