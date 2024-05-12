package redirector.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import redirector.Message;
import redirector.Secret;
import redirector.producers.ToMonitoring;
import redirector.producers.ToTravel;

import java.util.Objects;

@Component
@Slf4j
public class FromAiConnector {
    private final ToMonitoring prod;
    private final ToTravel prodToTravel;

    public FromAiConnector(ToMonitoring prod, ToTravel prodToTravel) {
        Objects.requireNonNull(prod);
        this.prod = prod;
        Objects.requireNonNull(prodToTravel);
        this.prodToTravel = prodToTravel;
    }

    @KafkaListener(
            topics = "ai-connector-redirector",
            groupId = "ai-connector-redirector",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Запрос на предполетную диагностику")) {
            prod.sendMessage(
                    new Message("Запрос на предполетную диагностику"),
                    "default",
                    "redirector",
                    "monitoring"
            );
        }

        if (msg.equals("Передача информации о полете")) {
            prodToTravel.sendMessage(
                    new Message("Передача информации о полете"),
                    "default",
                    "redirector",
                    "travel"
            );
        }

        if (msg.equals("Оружие подобрано")) {
            prodToTravel.sendMessage(
                    new Message("Оружие подобрано"),
                    "default",
                    "redirector",
                    "weapon"
            );
        }
    }
}
