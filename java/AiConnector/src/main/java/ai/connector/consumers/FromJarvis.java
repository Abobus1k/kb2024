package ai.connector.consumers;

import ai.connector.*;
import ai.connector.producers.ToJarvis;
import ai.connector.producers.ToRedirector;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromJarvis {
    private final ToJarvis prodToJarvis;
    private final ToRedirector prodToRedirector;

    public FromJarvis(ToJarvis prodToJarvis, ToRedirector prodToRedirector) {
        Objects.requireNonNull(prodToJarvis);
        this.prodToJarvis = prodToJarvis;
        Objects.requireNonNull(prodToJarvis);
        this.prodToRedirector = prodToRedirector;
    }

    @KafkaListener(
            topics = "jarvis-ai-connector",
            groupId = "jarvis-ai-connector",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(ConsumerRecord<String, Message> record) throws Exception {
        if (record.value().getMessage().equals("Передача информации о миссии")) {
            prodToJarvis.sendMessage(
                    new Message("Информация о миссии передана"),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        }

        if (record.value().getMessage().equals("Запрос на предполетную диагностику")) {
            prodToRedirector.sendMessage(
                    new Message("Запрос на предполетную диагностику"),
                    "default",
                    "ai-connector",
                    "redirector"
            );
        }

        if (record.value().getMessage().equals("Передача информации о полете")) {
            prodToRedirector.sendMessage(
                    new Message("Передача информации о полете"),
                    "default",
                    "ai-connector",
                    "redirector"
            );
        }

        if (record.value().getMessage().equals("Оружие подобрано")) {
            prodToRedirector.sendMessage(
                    new Message("Оружие подобрано"),
                    "default",
                    "ai-connector",
                    "redirector"
            );
        }
    }
}
