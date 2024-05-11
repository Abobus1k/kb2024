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
            groupId = "ai-connector-jarvis-mission-prepare",
            concurrency = "1",
            containerFactory = "missionPrepareRequestContainerFactory"
    )
    public void missionPrepare(final ConsumerRecord<String, MissionPrepareRequest> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getMissionPrepareRequest());
            String decryptedCommand = Secret.decrypt(record.value().getMissionPrepareRequest());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendMissionInfoRequest(
                    new MissionInfoRequest(
                            Secret.encrypt("Информация о миссии")
                    ),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }

    @KafkaListener(
            topics = "ai-connector-jarvis",
            groupId = "ai-connector-jarvis-mission-prepare-response",
            concurrency = "1",
            containerFactory = "missionInfoResponseContainerFactory"
    )
    public void missionResponse(final ConsumerRecord<String, MissionInfoResponse> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getMissionInfoResponse());
            String decryptedCommand = Secret.decrypt(record.value().getMissionInfoResponse());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendPreFlightRequest(
                    new PreFlightRequest(
                            Secret.encrypt("Запрос на предполетную диагностику")
                    ),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }

    @KafkaListener(
            topics = "ai-connector-jarvis",
            groupId = "ai-connector-jarvis-preflight-response",
            concurrency = "1",
            containerFactory = "preFlightResponseContainerFactory"
    )
    public void preFlightResponse(final ConsumerRecord<String, PreFlightResponse> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getPreFlightResponse());
            String decryptedCommand = Secret.decrypt(record.value().getPreFlightResponse());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendReadyForMissionRequest(
                    new ReadyForMission(
                            Secret.encrypt("Готов к миссии")
                    ),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
            prod.sendFlightInfoRequest(
                    new FlightInfoRequest(
                            Secret.encrypt("Информация о полете")
                    ),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }

    @KafkaListener(
            topics = "ai-connector-jarvis",
            groupId = "ai-connector-jarvis-weapon-request",
            concurrency = "1",
            containerFactory = "weaponActivationRequestContainerFactory"
    )
    public void weaponActivationRequest(final ConsumerRecord<String, WeaponActivationRequest> record) {
        log.info("Starting FromAiConnector");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            log.info("Encrypted command: {}", record.value().getWeaponActivationRequest());
            String decryptedCommand = Secret.decrypt(record.value().getWeaponActivationRequest());
            log.info("Decrypted command: {}", decryptedCommand);
            prod.sendWeaponResponse(
                    new WeaponActivationResponse(
                            Secret.encrypt("Оружие подобрано")
                    ),
                    "default",
                    "jarvis",
                    "ai-connector"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromAiConnector");
    }
}
