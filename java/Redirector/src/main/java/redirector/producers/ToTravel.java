package redirector.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import redirector.TravelCommand;

import java.util.Objects;

import static redirector.conf.Kafka.MAKE_TRAVEL;

@Component
@Slf4j
public class ToTravel extends Helper{
    private final KafkaTemplate<String, TravelCommand> kafka;

    public ToTravel(
            @Qualifier(MAKE_TRAVEL)
            final KafkaTemplate<String, TravelCommand> kafkaTemplate) {
        Objects.requireNonNull(kafkaTemplate);
        this.kafka = kafkaTemplate;
    }

    public void send(TravelCommand event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }
}
