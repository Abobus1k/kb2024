package travel.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import travel.Message;
import travel.Secret;
import travel.producers.ToGeo;

import java.util.Objects;

@Component
@Slf4j
public class FromRedirector {
    private final ToGeo prod;

    public FromRedirector(ToGeo prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "redirector-travel",
            groupId = "redirector-travel",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Передача информации о полете")) {
            prod.sendMessage(
                    new Message("Передача информации о полете"),
                    "default",
                    "travel",
                    "geo"
            );
        }
    }
}
