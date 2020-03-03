package com.income.robot.service.mq;


import com.alibaba.fastjson.JSON;
import com.bbin.common.util.ThreadLocalUtils;

import com.income.robot.service.interceptor.RobotThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqSenter {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendMessage(String exchange, String route, Object obj) {
      log.info("========================= RabbitMq ==============================" );
      log.info("MQ发送信息打印：{}", JSON.toJSONString(obj));
      ThreadLocalUtils.setTenantId(RobotThreadLocalUtils.getTenantId());
      ThreadLocalUtils.setChannelId(RobotThreadLocalUtils.getChannelId());
      try {
            this.rabbitTemplate.convertAndSend(exchange, route, obj);
      } finally {
            ThreadLocalUtils.clean();
      }
  }
}
