package ai.connector.consumers;

import ai.connector.*;
import ai.connector.producers.ToJarvis;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromJarvis {
    private final ToJarvis prod;

    public FromJarvis(ToJarvis prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "jarvis-ai-connector",
            groupId = "jarvis-ai-connector-mission-info-request",
            concurrency = "1",
            containerFactory = "missionInfoRequestContainerFactory"
    )
    public void missionInfoRequest(final ConsumerRecord<String, MissionInfoRequest> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getMissionInfoRequest());
            String decryptedCommand = Secret.decrypt(record.value().getMissionInfoRequest());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendMissionInfoResponse(
                    new MissionInfoResponse(
                            Secret.encrypt("Информация передана")
                    ),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }

    @KafkaListener(
            topics = "jarvis-ai-connector",
            groupId = "jarvis-ai-connector-preflight-request",
            concurrency = "1",
            containerFactory = "preFlightRequestContainerFactory"
    )
    public void preFlightRequest(final ConsumerRecord<String, PreFlightRequest> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getPreFlightRequest());
            String decryptedCommand = Secret.decrypt(record.value().getPreFlightRequest());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendPreFlightResponse(
                    new PreFlightResponse(
                            "Запрос на предполетную диагностику"
                    ),
                    "default",
                    "ai-connector",
                    "jarvis"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }
}
