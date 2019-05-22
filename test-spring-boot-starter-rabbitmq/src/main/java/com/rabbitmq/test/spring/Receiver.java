package com.rabbitmq.test.spring;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

  @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
  public void receiveMessage(String message) {
      System.out.println("Received <" + message + ">");
  }
}