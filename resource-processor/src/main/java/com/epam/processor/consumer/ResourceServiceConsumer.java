package com.epam.processor.consumer;

import com.epam.processor.config.MessageConfiguration;
import com.epam.processor.dto.Id;
import com.epam.processor.service.QueueDataProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ResourceServiceConsumer {

    private final QueueDataProcessor queueDataProcessor;

    @RabbitListener(queues = MessageConfiguration.RESOURCE_SERVICE_QUEUE)
    @Retryable(value = RuntimeException.class, maxAttempts = 1)
    public void consumeMessageFromQueue(Id id) {
        log.info("Listener consumes message from Resource Service Queue. Id: {}", id);
        try {
            queueDataProcessor.processResourceId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Recover
    public void recover(Throwable e, Id id) {
        if (e != null) {
            e.printStackTrace();
        }
        log.error("Error loading " + id + ". Exception " + e);
    }
}
