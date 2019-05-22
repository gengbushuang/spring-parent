package com.rabbitmq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

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
//        //是否持久化，持久化后服务重启后队列还存在
//        boolean durable = false;
//        //是否独占队列，如果是，只能这个连接可以访问这个队列
//        boolean exclusive = false;
//        //是否自动删除队列，如果是，服务不在使用时就删除
//        boolean autoDelete = false;
//        //队列其他的参数
//        Map<String, Object> arguments = null;
//        //声明队列
//        channel.queueDeclare(queueName,durable,exclusive,autoDelete,arguments);
        //是否自动应答，如果是，一旦传递就确认
        boolean autoAck = false;

        channel.basicConsume(queueName, autoAck, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();

                String message = new String(body);
                System.out.println(" [y] Received '" + message + "' routingKey '" + routingKey + "' contentType '" + contentType + "' deliveryTag '" + deliveryTag + "'");
                //如果为true就是正确提交以前到deliveryTag范围所有消息都应答
                //如果为false就是抛弃当前deliveryTag下标的消息，以前的消息还未应答
                boolean multiple = true;
                channel.basicAck(deliveryTag, multiple);
            }
        });

    }
}
