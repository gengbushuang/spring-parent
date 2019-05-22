package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

public class ProducerConfirms {

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

        //交换机名字
        String exchange = "exchange_confirms";

        //消息内容
        String message = "This is an important message";

        channel.exchangeDeclare(exchange, "fanout");
        //开启发送方确认消息
        channel.confirmSelect();

        //路由选择
        String routingKey = "important.message";

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .contentType("text/plain")
                .timestamp(new Date())
                .type("very important").build();

        channel.basicPublish(exchange, routingKey, properties, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        //异步监听确认和未确认的消息
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("未确认消息，标识：" + deliveryTag);
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));
            }
        });
        //关闭
        channel.close();
        connection.close();

    }
}
