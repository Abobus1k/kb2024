package redirector.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import redirector.Message;
import redirector.producers.ToAiConnector;
import redirector.producers.ToMonitoring;

import java.util.Objects;

@Component
@Slf4j
public class FromMonitoring {
    private final ToAiConnector prod;

    public FromMonitoring(ToAiConnector prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "monitoring-redirector",
            groupId = "monitoring-redirector",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        if (record.value().getMessage().equals("Передача актуальной информации о состоянии системы")) {
            prod.sendMessage(
                    new Message("Передача актуальной информации о состоянии системы"),
                    "default",
                    "redirector",
                    "ai-connector"
            );
        }
    }
}
