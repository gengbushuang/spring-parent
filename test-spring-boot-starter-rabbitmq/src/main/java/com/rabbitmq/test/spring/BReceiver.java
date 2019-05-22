package com.rabbitmq.test.spring;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BReceiver {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_B)
    public void breceiverMessage(String message) {
        System.out.println("接收到来自队列的信息 <" + message + ">");
    }
}