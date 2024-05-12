package monitoring.consumers;

import lombok.extern.slf4j.Slf4j;
import monitoring.Message;
import monitoring.Secret;
import monitoring.producers.ToRedirector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromRedirector {
    private final ToRedirector prod;

    public FromRedirector(ToRedirector prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "redirector-monitoring",
            groupId = "redirector-monitoring",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Запрос на предполетную диагностику")) {
            prod.sendMessage(
                    new Message("Передача актуальной информации о состоянии системы"),
                    "default",
                    "monitoring",
                    "redirector"
            );
        }
    }
}
