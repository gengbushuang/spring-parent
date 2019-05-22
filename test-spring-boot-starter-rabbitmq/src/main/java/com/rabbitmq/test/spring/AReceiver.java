package com.rabbitmq.test.spring;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AReceiver {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_A)
    public void areceiverMessage(String message) {
        System.out.println("接收到来自A队列的信息 <" + message + ">");
    }
}
