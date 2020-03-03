package com.income.robot.service.chain;

public abstract class Filter <T>{
  protected abstract boolean dofilter(Invoker invoker, T invocation) throws Exception;
  
  boolean doFilterFinal(Invoker invoker, T invocation) throws Exception {
    if (null == invoker) {
      return true;
    }
      return dofilter(invoker, invocation) ? invoker.invoke(invocation) : false;
  }
}
