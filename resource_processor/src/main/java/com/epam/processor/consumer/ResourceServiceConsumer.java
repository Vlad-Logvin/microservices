package com.epam.processor.consumer;

import com.epam.processor.config.MessageConfiguration;
import com.epam.processor.dto.Id;
import com.epam.processor.service.QueueDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResourceServiceConsumer {

    private final QueueDataProcessor queueDataProcessor;

    @Autowired
    public ResourceServiceConsumer(QueueDataProcessor queueDataProcessor) {
        this.queueDataProcessor = queueDataProcessor;
    }

    @RabbitListener(queues = MessageConfiguration.RESOURCE_SERVICE_QUEUE)
    @Retryable(value = RuntimeException.class, maxAttempts = 5, backoff = @Backoff(20000))
    public void consumeMessageFromQueue(Id id) {
        queueDataProcessor.processResourceId(id);
    }

    @Recover
    public void recover(RuntimeException e) {
        log.error("Error!", e);
    }
}
