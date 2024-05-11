package redirector.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import redirector.Connection;
import redirector.Secret;
import redirector.TravelCommand;
import redirector.producers.ToTravel;

import java.util.Objects;

@Component
@Slf4j
public class FromAiConnector {
    private final ToTravel prod;

    public FromAiConnector(ToTravel prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "ai-connector-redirector",
            groupId = "ai-connector-redirector",
            concurrency = "1",
            containerFactory = "makeTravel"
    )
    public void consume(final ConsumerRecord<String, Connection> record) {
        if (!Objects.equals(record.key(), "create-connection")) {
            return;
        }
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            String encryptedCommand = Secret.encrypt(record.value().getConnectionCommand());
            log.info("Encrypted command: {}", encryptedCommand);
            String decryptedCommand = Secret.decrypt(encryptedCommand);
            log.info("Decrypted command: {}", decryptedCommand);
            prod.send(
                    new TravelCommand(
                            encryptedCommand
                    ),
                    "default",
                    "redirector",
                    "maneuvr"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }
}
