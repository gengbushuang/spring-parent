package com.rabbitmq.test.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeoutException;

public class HashConsumer {
    public Connection getConnection() throws IOException, TimeoutException {
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
        return connection;
    }

    public String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    public void run() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();

        String pid = getPid();
        String queueName = String.format("hashing-worker-%s", pid);

        channel.queueDeclare(queueName, false, true, true, null);
        channel.queueBind(queueName, "fanout-rpc-requests", "#");

        channel.basicConsume(queueName, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();

                String messageHash = new String(body);
                System.out.println(" [HashConsumer] Received '" + messageHash + "' deliveryTag '" + deliveryTag + "'");

                channel.basicAck(deliveryTag, true);
            }
        });
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        HashConsumer hashConsumer = new HashConsumer();
        hashConsumer.run();
    }
}
