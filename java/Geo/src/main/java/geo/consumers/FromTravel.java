package geo.consumers;

import geo.Message;
import geo.producers.ToMonitoring;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromTravel {
    private final ToMonitoring prod;

    public FromTravel(ToMonitoring prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "travel-geo",
            groupId = "travel-geo",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        if (record.value().getMessage().equals("Передача информации о полете")) {
            prod.sendMessage(
                    new Message("Передача данных в интерфейс"),
                    "default",
                    "geo",
                    "monitoring"
            );

            prod.sendMessage(
                    new Message("Передача команды для перемещения"),
                    "default",
                    "geo",
                    "stabilizer"
            );

            prod.sendMessage(
                    new Message("Перемещение завершено"),
                    "default",
                    "geo",
                    "monitoring"
            );
        }
    }
}
