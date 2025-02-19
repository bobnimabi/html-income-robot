package com.income.robot.service.chain;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public abstract class Invoker {
    abstract <T>boolean invoke(T invocation) throws Exception;
  
    public static final Invoker buildInvokerChain(List<Filter> filters) {
      Invoker last = null;
      if (!filters.isEmpty())
        for (int i = filters.size() - 1; i >= 0; i--) {
          final Filter filter = filters.get(i);
          final Invoker next = last;
          last = new Invoker() {
              @Override
              <T> boolean invoke(T invocation) throws Exception {
                return filter.doFilterFinal(next, invocation);
              }
          };
      }
      return last;
    }
}
