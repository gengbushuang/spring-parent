package com.rabbitmq.test.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class WorkeRpc {

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

    public void querue(Channel channel, String exchangeName, String routingKey) throws IOException {
        String pid = getPid();
        String queueName = String.format("rpc-worker-%s", pid);
        System.out.println(queueName);
        channel.queueDeclare(queueName, false, true, true, null);
        //交换机direct-rpc-requests于队列rpc-worker-%s绑定，路由为detect-faces
        channel.queueBind(queueName, exchangeName, routingKey);
        //接收队列rpc-worker-%s消息
        channel.basicConsume(queueName, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String replyTo = properties.getReplyTo();

                Map<String,Object> headersMap = new HashMap<>();
                headersMap.put("first_publish",properties.getTimestamp());

                AMQP.BasicProperties workeProp = new AMQP.BasicProperties().builder()
                        .appId("Chapter 6 Listing 2 Consumer")
                        .contentType(properties.getContentType())
                        .correlationId(properties.getCorrelationId())
                        .headers(headersMap)
                        .build();

                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                String messageReceive = new String(body);
                System.out.println(" [WorkeRpc] Received '" + messageReceive + "' routingKey '" + routingKey + "' contentType '" + contentType + "' deliveryTag '" + deliveryTag + "'");

                String messageSend = deliveryTag+" worker message";
                //消息发送到交换器rpc-replies，路由为response-queue-%s
                channel.basicPublish("rpc-replies",replyTo,workeProp,messageSend.getBytes());

                channel.basicAck(deliveryTag, true);

            }
        });
    }

    public void run() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "direct-rpc-requests";
        String routingKey = "detect-faces";
        querue(channel, exchangeName, routingKey);

        //关闭
//        channel.close();
//        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        WorkeRpc workeRpc = new WorkeRpc();
        workeRpc.run();
    }
}
