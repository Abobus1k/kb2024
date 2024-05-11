package jarvis.producers;

import jarvis.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static jarvis.conf.Kafka.*;

@Component
@Slf4j
public class ToAiConnector extends Helper {
    private final KafkaTemplate<String, MissionInfoRequest> missionInfoRequestKafkaTemplate;
    private final KafkaTemplate<String, PreFlightRequest> preFlightRequestKafkaTemplate;
    private final KafkaTemplate<String, ReadyForMission> readyForMissionKafkaTemplate;
    private final KafkaTemplate<String, FlightInfoRequest> flightInfoRequestKafkaTemplate;
    private final KafkaTemplate<String, WeaponActivationResponse> weaponActivationResponseKafkaTemplate;

    public ToAiConnector(
            @Qualifier(SEND_MISSION_INFO_REQUEST)
            final KafkaTemplate<String, MissionInfoRequest> kafkaTemplate,
            @Qualifier(PRE_FLIGHT_REQUEST)
            final KafkaTemplate<String, PreFlightRequest> preFlightRequestKafkaTemplate,
            @Qualifier(READY_FOR_MISSION)
            final KafkaTemplate<String, ReadyForMission> readyForMissionKafkaTemplate,
            @Qualifier(FLIGHT_INFO_REQUEST)
            final KafkaTemplate<String, FlightInfoRequest> flightInfoRequestKafkaTemplate,
            @Qualifier(WEAPON_RESPONSE)
            final KafkaTemplate<String, WeaponActivationResponse> weaponActivationResponseKafkaTemplate) {
        Objects.requireNonNull(kafkaTemplate);
        this.missionInfoRequestKafkaTemplate = kafkaTemplate;
        Objects.requireNonNull(preFlightRequestKafkaTemplate);
        this.preFlightRequestKafkaTemplate = preFlightRequestKafkaTemplate;
        Objects.requireNonNull(readyForMissionKafkaTemplate);
        this.readyForMissionKafkaTemplate = readyForMissionKafkaTemplate;
        Objects.requireNonNull(flightInfoRequestKafkaTemplate);
        this.flightInfoRequestKafkaTemplate = flightInfoRequestKafkaTemplate;
        Objects.requireNonNull(weaponActivationResponseKafkaTemplate);
        this.weaponActivationResponseKafkaTemplate = weaponActivationResponseKafkaTemplate;
    }

    public void sendMissionInfoRequest(MissionInfoRequest event, String key, String from, String to) {
        log.info("Start sending new mission info request: {}", event);
        missionInfoRequestKafkaTemplate.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }

    public void sendPreFlightRequest(PreFlightRequest event, String key, String from, String to) {
        log.info("Start sending preflight request: {}", event);
        preFlightRequestKafkaTemplate.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }

    public void sendReadyForMissionRequest(ReadyForMission event, String key, String from, String to) {
        log.info("Start sending ready for mission request: {}", event);
        readyForMissionKafkaTemplate.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }

    public void sendFlightInfoRequest(FlightInfoRequest event, String key, String from, String to) {
        log.info("Start sending flight info request: {}", event);
        flightInfoRequestKafkaTemplate.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }

    public void sendWeaponResponse(WeaponActivationResponse event, String key, String from, String to) {
        log.info("Start sending weapon activation response: {}", event);
        weaponActivationResponseKafkaTemplate.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }
}
