package jarvis.consumers;

import jarvis.*;
import jarvis.producers.ToAiConnector;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromAiConnector {
    private final ToAiConnector prod;

    public FromAiConnector(ToAiConnector prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "ai-connector-jarvis",
            groupId = "ai-connector-jarvis",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(final ConsumerRecord<String, Message> record) throws Exception {
        if (record.value().getMessage().equals("Запрос на начало подготовки к миссии")) {
            prod.sendMessage(
                    new Message("Передача информации о миссии"),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        }

        if (record.value().getMessage().equals("Информация о миссии передана")) {
            prod.sendMessage(
                    new Message("Запрос на предполетную диагностику"),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        }

        if (record.value().getMessage().equals("Передача актуальной информации о состоянии системы")) {
            prod.sendMessage(
                    new Message("Готов к миссии"),
                    "default",
                    "jarvis",
                    "ai-connector"
            );

            prod.sendMessage(
                    new Message("Передача информации о полете"),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        }

        if (record.value().getMessage().equals("Запрос на активацию оружия")) {
            prod.sendMessage(
                    new Message("Оружие подобрано"),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        }
    }
}
