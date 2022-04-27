package com.epam.resource.publisher;

import com.epam.resource.configuration.MessageConfiguration;
import com.epam.resource.exception.impl.RabbitException;
import com.epam.resource.util.Id;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public record ResourcePublisher(RabbitTemplate rabbitTemplate) {
    public void sendToResourceProcessor(Id id) {
        try {
            rabbitTemplate.convertAndSend(MessageConfiguration.RESOURCE_SERVICE_EXCHANGE, MessageConfiguration.RESOURCE_SERVICE_KEY, id);
        } catch (AmqpException e) {
            e.printStackTrace();
            throw new RabbitException(e, "Exception was thrown during sending id " + id + " to resource processor!", 500);
        }
    }
}
