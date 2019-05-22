package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ProducerMandatory {

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
        String queueName = "q_test_mandatory";
        //消息内容
        String message = "server.cpu.utilization 25.5 1350884514";
        //交换机
        String exchange = queueName;
        //路由选择
        String routingKey = "server-metrics";

        boolean mandatory = true;

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .contentType("text/plain")
                .timestamp(new Date())
                .type("graphite metric").build();

        channel.basicPublish(exchange,routingKey,mandatory,properties,message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        //关闭
        channel.close();
        connection.close();

    }
}
