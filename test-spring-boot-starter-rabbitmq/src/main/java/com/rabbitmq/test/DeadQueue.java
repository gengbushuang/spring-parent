package com.rabbitmq.test;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DeadQueue {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器地址
        factory.setHost("192.168.3.11");
        //端口
        factory.setPort(32771);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("test-hosts");
        factory.setUsername("test-guest");
        factory.setPassword("test-guest");
        //获取连接
        Connection connection = factory.newConnection();
        //从连接中获取通道
        Channel channel = connection.createChannel();

        //声明死信交换机
        channel.exchangeDeclare("first-delay-dead-exchange", BuiltinExchangeType.DIRECT, true, false, false, null);
        //声明死信队列
        channel.queueDeclare("first-delay-dead-queen", true, false, false, null);
        //绑定死信队列和死信交换机
        channel.queueBind("first-delay-dead-queen", "first-delay-dead-exchange", "first-delay-dead", null);

        //声明延迟交换机
        channel.exchangeDeclare("first-delay-exchange", BuiltinExchangeType.DIRECT, true, false, false, null);
        //声明延迟队列
        Map<String, Object> map = new HashMap<>();
        //标志队列中的消息存活时间，也就是说队列中的消息超过了指定时间会被删除(数字类型，标志时间，以豪秒为单位)
        map.put("x-message-ttl", 3000);
        /*
         消息因为超时或超过限制在队列里消失，这样我们就丢失了一些消息，也许里面就有一些是我们做需要获知的。
         而rabbitmq的死信功能则为我们带来了解决方案。
         设置了dead letter exchange与dead letter routingkey（要么都设定，要么都不设定）那些因为超时或超出限制而被删除的消息会被推动到我们设置的exchange中，
        再根据routingkey推到queue中.
         */
        map.put("x-dead-letter-exchange", "first-delay-dead-exchange");
        map.put("x-dead-letter-routing-key", "first-delay-dead");

        channel.queueDeclare("first-delay-queen", true, false, false, map);
        //绑定延迟队列和延迟交换机
        channel.queueBind("first-delay-queen", "first-delay-exchange", "first-delay", null);

    }
}
