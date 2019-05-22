package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerGet {

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
        //队列名字
        String queueName = "q_test";
        //是否自动应答，如果是，一旦传递就确认
        boolean autoAck = false;
        while (true) {
            GetResponse getResponse = channel.basicGet(queueName, autoAck);
            if (getResponse == null) {
                continue;
            }
            long deliveryTag = getResponse.getEnvelope().getDeliveryTag();
            byte[] body = getResponse.getBody();
            String message = new String(body);
            System.out.println(" [y] Received '" + message + "' deliveryTag '" + deliveryTag + "'");
            if (!autoAck) {
                channel.basicAck(deliveryTag, autoAck);
            }
        }

    }
}
