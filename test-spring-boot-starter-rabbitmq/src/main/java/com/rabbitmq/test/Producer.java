package com.rabbitmq.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Producer {

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
        //是否持久化，持久化后服务重启后队列还存在
        boolean durable = false;
        //是否独占队列，如果是，只能这个连接可以访问这个队列
        boolean exclusive = false;
        //是否自动删除队列，如果是，服务不在使用时就删除
        boolean autoDelete = false;
        //队列其他的参数
        Map<String, Object> arguments = null;
        //创建队列
        channel.queueDeclare(queueName,durable,exclusive,autoDelete,arguments);
        //消息内容
        String message = "hello world";
        //交换机
        String exchange = "";
        //路由选择
        String routingKey = queueName;
        channel.basicPublish(exchange,routingKey,null,message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        //关闭
        channel.close();
        connection.close();

    }
}
