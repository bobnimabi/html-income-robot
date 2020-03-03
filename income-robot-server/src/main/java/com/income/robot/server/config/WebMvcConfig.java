package com.income.robot.server.config;


import com.income.robot.service.interceptor.TenantInterceptor;
import com.income.robot.service.config.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends WebConfig {

  @Autowired
  private TenantInterceptor tenantInterceptor;
  
  public void addInterceptors(InterceptorRegistry registry) {
    super.addInterceptors(registry);
    registry.addInterceptor(tenantInterceptor).addPathPatterns(new String[] { "/**" });
  }
}
