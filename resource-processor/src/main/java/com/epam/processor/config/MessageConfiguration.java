package com.epam.processor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
@EnableRetry
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

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
