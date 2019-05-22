package com.rabbitmq.test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ProducerBackups {

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
        String exchange1 = "my-ae";
        String type1 = "fanout";
        //创建交换器
        channel.exchangeDeclare(exchange1, type1,true,false,null);

        String exchange2 = "graphite";
        String type2 = "topic";
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("alternate-exchange","my_ae.name");

        channel.exchangeDeclare(exchange2,type2,true,false,arguments);

        String queueName = "unroutable-message";
        channel.queueDeclare(queueName,true, false, false,null);

        channel.queueBind(queueName,exchange2,"#");
        //关闭
        channel.close();
        connection.close();

    }
}
