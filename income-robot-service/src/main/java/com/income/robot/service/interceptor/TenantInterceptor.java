package com.income.robot.service.interceptor;


import com.alibaba.fastjson.JSON;
import com.bbin.common.dto.domain.DomainDTO;
import com.bbin.common.feign.PromotionClient;
import com.bbin.common.response.ResponseResult;
import com.bbin.common.util.ThreadLocalUtils;
import com.bbin.common.vo.DomainQuryVO;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.income.robot.service.interceptor.RobotThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Service
public class TenantInterceptor extends HandlerInterceptorAdapter {
  @Autowired
  private PromotionClient promotionClient;
  
  private static final String BACK_PATH = "/imageCode/verifyImageCode";
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {
      ThreadLocalUtils.clean();
      String uri = request.getRequestURI();
      if (uri.indexOf("/imageCode/verifyImageCode") != -1) {
        setFromHeader(request, "tenant_id");
        setFromHeader(request, "channel_id");
      } else {
        setKeyFromDomain(request);
      } 
      RobotThreadLocalUtils.setTenantId(ThreadLocalUtils.getTenantIdOption().get());
      RobotThreadLocalUtils.setChannelId(ThreadLocalUtils.getChannelIdOption().get());
      ThreadContext.put("TENANT_ID", RobotThreadLocalUtils.getTenantId() + "");
      ThreadContext.put("CHANNEL_ID", RobotThreadLocalUtils.getChannelId() + "");
      return true;
    } catch (Exception e) {
      log.error("设置tenantId和channelId异常", e);
      return false;
    } 
  }
  
  private void setFromHeader(HttpServletRequest request, String key) {
    String tenantId = request.getHeader(key);
    Optional.<String>ofNullable(tenantId).map(a -> {
          ThreadLocalUtils.set(key, a);
          return a;
        }).orElseThrow(() -> new IllegalArgumentException(key +"不存在"));
  }
  
  private void setKeyFromDomain(HttpServletRequest request) {
    String domain = request.getServerName();
    DomainDTO domainDTO = new DomainDTO();
    domainDTO.setDomain(domain);
    log.info("domain=[{}]", domain);
    ResponseResult responseResult = promotionClient.get(domainDTO);
    log.info("responseResult=[{}]", responseResult);
    if (responseResult.isSuccess()) {
      DomainQuryVO domainQuryVO = (DomainQuryVO)JSON.parseObject(responseResult.getObj().toString(), DomainQuryVO.class);
      Optional.<DomainQuryVO>ofNullable(domainQuryVO).map(a -> {
            ThreadLocalUtils.set("tenant_id", a.getTenantId());
            ThreadLocalUtils.set("channel_id", a.getChannelId());
            return a;
          }).orElseThrow(() -> new IllegalArgumentException("根据域名未获取到tenantId或channelId"));
    } else {
      log.error("异常：根据域名未获取到tenantId或channelId");
      throw new IllegalArgumentException("异常：根据域名未获取到tenantId或channelId");
    } 
  }
  
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    ThreadLocalUtils.clean();
    ThreadContext.clearMap();
  }
}
