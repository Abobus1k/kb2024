package stabilizer.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import stabilizer.Message;
import stabilizer.Secret;

import java.util.Objects;

import static stabilizer.conf.Kafka.MESSAGE;


@Component
@Slf4j
public class ToStabilizer extends Helper {
    private final KafkaTemplate<String, Message> messageKafkaTemplate;

    public ToStabilizer(
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
