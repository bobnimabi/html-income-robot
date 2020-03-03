package com.income.robot.service.config;



import com.income.robot.service.interceptor.FlushTokenInterceptor;
import com.income.robot.service.interceptor.LoginLogInterceptor;
import com.income.robot.service.interceptor.UnLoginLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class WebConfig implements WebMvcConfigurer {
  private CorsConfiguration buildConfig() {
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.addAllowedOrigin("*");
      corsConfiguration.setAllowCredentials(Boolean.valueOf(true));
      corsConfiguration.addAllowedHeader("*");
      corsConfiguration.addAllowedMethod("*");
      return corsConfiguration;
  }
  
  @Bean
  public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", buildConfig());
      return new CorsFilter((CorsConfigurationSource)source);
  }
  
  @Bean
  public HandlerInterceptor getLoginLogInterceptor() {
      return (HandlerInterceptor)new LoginLogInterceptor();
  }
  
  @Bean
  public HandlerInterceptor getUnLoginLogInterceptor() {
      return (HandlerInterceptor)new UnLoginLogInterceptor();
  }
  
  @Bean
  public FlushTokenInterceptor getFlushTokenInterceptor() {
      return new FlushTokenInterceptor();
  }
  
  public void addInterceptors(InterceptorRegistry registry) {
      String[] path = { "/**/addRobot", "/**/deleteRobot", "/**/updateRobot", "/**/pageRobot", "/**/getRobotById", "/**/closeRobot" };
      registry.addInterceptor(getUnLoginLogInterceptor())
        .addPathPatterns(new String[] { "/**" });
  }
}
