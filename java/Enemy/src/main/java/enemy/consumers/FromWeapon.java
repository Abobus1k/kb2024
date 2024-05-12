package enemy.consumers;

import enemy.Message;
import enemy.producers.ToWeapon;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromWeapon {
    private final ToWeapon prod;

    public FromWeapon(ToWeapon prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "weapon-enemy",
            groupId = "weapon-enemy",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        if (record.value().getMessage().equals("Оружие применено")) {
            prod.sendMessage(
                    new Message("Враг поражен"),
                    "default",
                    "enemy",
                    "weapon"
            );
        }
    }
}
