package ai.connector.producers;

import ai.connector.Message;
import ai.connector.Secret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static ai.connector.conf.Kafka.MESSAGE;

@Component
@Slf4j
public class ToRedirector extends Helper {
    private final KafkaTemplate<String, Message> messageKafkaTemplate;

    public ToRedirector(
            @Qualifier(MESSAGE)
            final KafkaTemplate<String, Message> messageKafkaTemplate

    ) {
        Objects.requireNonNull(messageKafkaTemplate);
        this.messageKafkaTemplate = messageKafkaTemplate;
    }
    public void sendMessage(Message event, String key, String from, String to) throws Exception {
        Message message = new Message(Secret.encrypt(event.getMessage()));
        messageKafkaTemplate.send(formMessage(event, key, from, to));
    }
}
