package com.rabbitmq.test.spring;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_NAME = "spring-boot";
    public static final String QUEUE_NAME_A = "spring-boot-direct-a";
    public static final String QUEUE_NAME_B = "spring-boot-direct-b";
    public static final String QUEUE_EXCHANGE_NAME = "spring-boot-exchange";
    public static final String QUEUE_EXCHANGE_NAME_DIRECT = "spring-boot-exchange-direct";

    ////////////////////////////////////////////////TopicExchange
    @Bean
    public Queue queue() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_NAME, durable, exclusive, autoDelete);
    }

    @Bean
    public TopicExchange exchange() {
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new TopicExchange(QUEUE_EXCHANGE_NAME, durable, autoDelete);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    //////////////////////////////////////////////DirectExchange
    @Bean
    Queue aQueue() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_NAME_A, durable, exclusive, autoDelete);
    }

    @Bean
    Queue bQueue() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(QUEUE_NAME_B, durable, exclusive, autoDelete);
    }

    @Bean
    public DirectExchange directExchange() {
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new DirectExchange(QUEUE_EXCHANGE_NAME_DIRECT, durable, autoDelete);
    }

    @Bean
    Binding bindingA(Queue aQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(aQueue)
                .to(directExchange)
                .with(QUEUE_NAME_A);
    }

    @Bean
    Binding bindingB(Queue bQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(bQueue)
                .to(directExchange)
                .with(QUEUE_NAME_B);
    }

    @Bean
    SimpleMessageListenerContainer aContainer(ConnectionFactory connectionFactory,
                                              MessageListenerAdapter aListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_A);
        container.setMessageListener(aListenerAdapter);
        return container;
    }

    @Bean
    SimpleMessageListenerContainer bContainer(ConnectionFactory connectionFactory,
                                              MessageListenerAdapter bListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_B);
        container.setMessageListener(bListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter aListenerAdapter(AReceiver areceiver) {
        return new MessageListenerAdapter(areceiver, "areceiverMessage");
    }

    @Bean
    MessageListenerAdapter bListenerAdapter(BReceiver breceiver) {
        return new MessageListenerAdapter(breceiver, "breceiverMessage");
    }

}