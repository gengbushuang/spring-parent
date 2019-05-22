package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerConsume {

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
//        channel.basicQos(10,false);
        channel.basicConsume(queueName, autoAck, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();

                String message = new String(body);
                System.out.println(" [y] Received '" + message + "' routingKey '" + routingKey + "' contentType '" + contentType + "' deliveryTag '" + deliveryTag + "'");
//                if(deliveryTag%2==0){
////                    channel.basicAck(deliveryTag,false);
//                    channel.basicReject(deliveryTag,true);
//                }
//                channel.basicAck(deliveryTag,false);
            }
        });

    }
}
