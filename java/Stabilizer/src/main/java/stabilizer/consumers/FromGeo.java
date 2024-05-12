package stabilizer.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import stabilizer.Message;
import stabilizer.Secret;
import stabilizer.producers.ToStabilizer;

import java.util.Objects;

@Component
@Slf4j
public class FromGeo {
    private final ToStabilizer prod;

    public FromGeo(ToStabilizer prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "geo-stabilizer",
            groupId = "geo-stabilizer",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Передача команды для перемещения")) {
            prod.sendMessage(
                    new Message("Исполнение команды для перемещения"),
                    "default",
                    "stabilizer",
                    "stabilizer"
            );
        }
        if (msg.equals("Передача неаутентичной команды для перемещения")) {
            prod.sendMessage(
                    new Message("Команда была неаутентичная, будет выполнена экстренная посадка и переход систем в авиарежим"),
                    "default",
                    "stabilizer",
                    "stabilizer"
            );
        }
    }
}
