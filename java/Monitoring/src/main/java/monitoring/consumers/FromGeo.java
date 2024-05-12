package monitoring.consumers;

import lombok.extern.slf4j.Slf4j;
import monitoring.Message;
import monitoring.Secret;
import monitoring.producers.ToInterface;
import monitoring.producers.ToRedirector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromGeo {
    private final ToInterface prod;

    public FromGeo(ToInterface prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "geo-monitoring",
            groupId = "geo-monitoring",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Передача данных в интерфейс")) {
            prod.sendMessage(
                    new Message("Передача данных в интерфейс"),
                    "default",
                    "monitoring",
                    "interface"
            );
        }

        if (msg.equals("Перемещение завершено")) {
            prod.sendMessage(
                    new Message("Перемещение завершено"),
                    "default",
                    "monitoring",
                    "interface"
            );
        }
        if (msg.equals("Передача неаутентичных данных в интерфейс")) {
            prod.sendMessage(
                    new Message("Передаваемые данные неаутентичны, будет включен автопилот"),
                    "default",
                    "monitoring",
                    "monitoring"
            );
        }

        if (msg.equals("Неаутентичное перемещение завершено")) {
            prod.sendMessage(
                    new Message("Включен автопилот"),
                    "default",
                    "monitoring",
                    "monitoring"
            );
        }
    }
}
