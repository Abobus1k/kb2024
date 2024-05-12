package weapon.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import weapon.Message;
import weapon.Secret;
import weapon.producers.ToWeapon;

import java.util.Objects;

@Component
@Slf4j
public class FromRedirector {
    private final ToWeapon prod;

    public FromRedirector(ToWeapon prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "redirector-weapon",
            groupId = "redirector-weapon",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Оружие подобрано")) {
            prod.sendMessage(
                    new Message("Оружие активировано"),
                    "action",
                    "weapon",
                    "weapon"
            );

            prod.sendMessage(
                    new Message("Оружие применено"),
                    "action",
                    "weapon",
                    "enemy"
            );
        }
    }
}
