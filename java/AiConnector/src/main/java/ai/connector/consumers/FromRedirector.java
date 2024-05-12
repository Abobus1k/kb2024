package ai.connector.consumers;

import ai.connector.Message;
import ai.connector.Secret;
import ai.connector.producers.ToJarvis;
import ai.connector.producers.ToRedirector;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromRedirector {
    private final ToJarvis prodToJarvis;
    private final ToRedirector prodToRedirector;

    public FromRedirector(ToJarvis prodToJarvis, ToRedirector prodToRedirector) {
        Objects.requireNonNull(prodToJarvis);
        this.prodToJarvis = prodToJarvis;
        Objects.requireNonNull(prodToJarvis);
        this.prodToRedirector = prodToRedirector;
    }

    @KafkaListener(
            topics = "redirector-ai-connector",
            groupId = "redirector-ai-connector",
            concurrency = "1",
            containerFactory = "messageContainerFactory"
    )
    public void consume(ConsumerRecord<String, Message> record) throws Exception {
        String msg = Secret.decrypt(record.value().getMessage());

        if (msg.equals("Передача актуальной информации о состоянии системы")) {
            prodToJarvis.sendMessage(
                    new Message("Передача актуальной информации о состоянии системы"),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        }
    }
}