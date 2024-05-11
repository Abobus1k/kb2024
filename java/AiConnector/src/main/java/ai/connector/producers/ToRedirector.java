package ai.connector.producers;

import ai.connector.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static ai.connector.conf.Kafka.CREATE_AI_CONNECTION;

@Component
@Slf4j
public class ToRedirector extends Helper{
    private final KafkaTemplate<String, Connection> kafka;

    public ToRedirector(
            @Qualifier(CREATE_AI_CONNECTION)
            final KafkaTemplate<String, Connection> kafkaTemplate) {
        Objects.requireNonNull(kafkaTemplate);
        this.kafka = kafkaTemplate;
    }

    public void send(Connection event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }
}
