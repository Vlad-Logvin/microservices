package com.epam.resource.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class MessageConfiguration {

    public static final String RESOURCE_SERVICE_QUEUE = "ResourceServiceQueue";
    public static final String RESOURCE_SERVICE_EXCHANGE = "ResourceServiceExchange";
    public static final String RESOURCE_SERVICE_KEY = "ResourceServiceKey";

    @Bean
    public Queue queue() {
        return new Queue(RESOURCE_SERVICE_QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RESOURCE_SERVICE_EXCHANGE);
    }

    @Bean
    public Binding binding(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(RESOURCE_SERVICE_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
