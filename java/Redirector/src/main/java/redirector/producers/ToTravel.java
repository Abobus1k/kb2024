package redirector.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import redirector.Message;
import redirector.Secret;

import java.util.Objects;

import static redirector.conf.Kafka.MESSAGE;

@Component
@Slf4j
public class ToTravel extends Helper{
    private final KafkaTemplate<String, Message> messageKafkaTemplate;

    public ToTravel(
            @Qualifier(MESSAGE)
            final KafkaTemplate<String, Message> messageKafkaTemplate) {
        Objects.requireNonNull(messageKafkaTemplate);
        this.messageKafkaTemplate = messageKafkaTemplate;
    }

    public void sendMessage(Message event, String key, String from, String to) throws Exception {
        Message message = new Message(Secret.encrypt(event.getMessage()));
        messageKafkaTemplate.send(formMessage(message, key, from, to));
    }
}
