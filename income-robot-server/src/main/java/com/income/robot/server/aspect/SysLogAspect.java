package com.income.robot.server.aspect;

import com.alibaba.fastjson.JSON;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SysLogAspect {

  @Pointcut("execution(public * com.income.robot.server.controller.Controller.*(..))")
  public void logPointCut() {}
  
  @Around("logPointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
    Object result = point.proceed();
    log.info("接口响应：{}", JSON.toJSONString(result));
    return result;
  }

  
  private String getMethod(ProceedingJoinPoint joinPoint) {
    try {
      Signature sig = joinPoint.getSignature();
      MethodSignature msig = null;
      if (!(sig instanceof MethodSignature))
        throw new IllegalArgumentException("MethodSignature不可用");
      msig = (MethodSignature)sig;
      Object target = joinPoint.getTarget();
      Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
      return currentMethod.getName();
    } catch (Exception exception) {
      return "test";
    } 
  }
}
