package com.rabbitmq.test.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class PublisherRpc {

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

    public void exchange(Channel channel, String[] exchangeNames) throws IOException {
        for (String exchangeName : exchangeNames) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        }
    }

    public void run() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "rpc-replies";
        String routingKey = "detect-faces";
        exchange(channel, new String[]{exchangeName});
        queue(channel, exchangeName, routingKey);

        //关闭
        channel.close();
        connection.close();
    }

    public String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;
    }

    public void queue(Channel channel, String exchangeName, String routingKey) throws IOException {
        String pid = getPid();
        String queueName = String.format("response-queue-%s", pid);
        channel.queueDeclare(queueName, false, true, true, null);
        //交换器rpc-replies绑定队列response-queue-%s，路由为response-queue-%s
        channel.queueBind(queueName, exchangeName, queueName);

        AMQP.BasicProperties publisherProp = new AMQP.BasicProperties().builder()
                .contentType("img")
                .correlationId(UUID.randomUUID().toString())
                .replyTo(queueName)
                .build();
        String publisherMessage = UUID.randomUUID().toString() + " publisher message";
        //消息发送到交换器direct-rpc-requests，路由为detect-faces
        channel.basicPublish("direct-rpc-requests", routingKey, publisherProp, publisherMessage.getBytes());

        GetResponse getResponse = null;
        while (getResponse==null) {
            //接收队列response-queue-%s消息
            getResponse = channel.basicGet(queueName, false);
            if (getResponse == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        long deliveryTag = getResponse.getEnvelope().getDeliveryTag();
        byte[] body = getResponse.getBody();
        String message = new String(body);
        System.out.println(" [PublisherRpc] Received '" + message + "' deliveryTag '" + deliveryTag + "'");
        channel.basicAck(deliveryTag, true);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        PublisherRpc publisherRpc = new PublisherRpc();
        publisherRpc.run();
    }
}
