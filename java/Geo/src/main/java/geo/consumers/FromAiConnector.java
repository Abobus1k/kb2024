package geo.consumers;

import geo.Message;
import geo.Secret;
import geo.producers.ToMonitoring;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

@Component
@Slf4j
public class FromAiConnector {
    private final ToMonitoring prod;

    public FromAiConnector(ToMonitoring prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "ai-connector-geo",
            groupId = "ai-connector-geo",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Негативный кейс")) {
            prod.sendMessage(
                    new Message("Передача неаутентичных данных в интерфейс"),
                    "default",
                    "geo",
                    "monitoring"
            );

            prod.sendMessage(
                    new Message("Передача неаутентичной команды для перемещения"),
                    "default",
                    "geo",
                    "stabilizer"
            );

            prod.sendMessage(
                    new Message("Неаутентичное перемещение завершено"),
                    "default",
                    "geo",
                    "monitoring"
            );
        }
    }
}
