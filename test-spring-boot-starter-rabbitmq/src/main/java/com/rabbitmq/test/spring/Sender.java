package com.rabbitmq.test.spring;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        System.out.println("ELSE 发送消息...");
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, "你好， ELSE!");
    }

    public void send(String RouteKey) {
        System.out.println("ELSE " + RouteKey + " 发送消息...");
        this.rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_EXCHANGE_NAME_DIRECT,RouteKey, "你好， ELSE!" + RouteKey);
    }
}