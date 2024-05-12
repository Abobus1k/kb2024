package geo.producers;

import geo.Message;
import geo.Secret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static geo.conf.Kafka.MESSAGE;

@Component
@Slf4j
public class ToMonitoring extends Helper {
    private final KafkaTemplate<String, Message> messageKafkaTemplate;

    public ToMonitoring(
            @Qualifier(MESSAGE)
            final KafkaTemplate<String, Message> messageKafkaTemplate) {
        Objects.requireNonNull(messageKafkaTemplate);
        this.messageKafkaTemplate = messageKafkaTemplate;
    }

    public void sendMessage(Message event, String key, String from, String to) throws Exception {
        Message message = new Message(Secret.encrypt(event.getMessage()));
        messageKafkaTemplate.send(formMessage(event, key, from, to));
    }
}
