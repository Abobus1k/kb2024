package ai.connector.producers;

import ai.connector.MissionInfoResponse;
import ai.connector.MissionPrepareRequest;
import ai.connector.PreFlightResponse;
import ai.connector.Secret;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static ai.connector.conf.Kafka.*;

@Component
@Slf4j
public class ToJarvis extends Helper {
    private final KafkaTemplate<String, MissionPrepareRequest> missionPrepareRequestKafkaTemplate;
    private final KafkaTemplate<String, MissionInfoResponse> missionInfoResponseKafkaTemplate;
    private final KafkaTemplate<String, PreFlightResponse> preFlightResponseKafkaTemplate;

    public ToJarvis(
            @Qualifier(MISSION_PREPARE_REQUEST)
            final KafkaTemplate<String, MissionPrepareRequest> missionPrepareRequestKafkaTemplate,
            @Qualifier(MISSION_INFO_RESPONSE)
            final KafkaTemplate<String, MissionInfoResponse> missionInfoResponseKafkaTemplate,
            @Qualifier(PREFLIGHT_RESPONSE)
            final KafkaTemplate<String, PreFlightResponse> preFlightResponseKafkaTemplate
    ) {
        Objects.requireNonNull(missionPrepareRequestKafkaTemplate);
        this.missionPrepareRequestKafkaTemplate = missionPrepareRequestKafkaTemplate;
        Objects.requireNonNull(missionInfoResponseKafkaTemplate);
        this.missionInfoResponseKafkaTemplate = missionInfoResponseKafkaTemplate;
        Objects.requireNonNull(preFlightResponseKafkaTemplate);
        this.preFlightResponseKafkaTemplate = preFlightResponseKafkaTemplate;
    }

    public void sendMissionPrepareRequest(MissionPrepareRequest event, String key, String from, String to) throws Exception {
        MissionPrepareRequest missionPrepareRequest = new MissionPrepareRequest(Secret.encrypt(event.getMissionPrepareRequest()));
        missionPrepareRequestKafkaTemplate.send(formMessage(missionPrepareRequest, key, from, to));
    }

    public void sendMissionInfoResponse(MissionInfoResponse event, String key, String from, String to) throws Exception {
        MissionInfoResponse missionInfoResponse = new MissionInfoResponse(Secret.encrypt(event.getMissionInfoResponse()));
        missionInfoResponseKafkaTemplate.send(formMessage(missionInfoResponse, key, from, to));
    }

    public void sendPreFlightResponse(PreFlightResponse event, String key, String from, String to) throws Exception {
        PreFlightResponse preFlightResponse = new PreFlightResponse(Secret.encrypt(event.getPreFlightResponse()));
        preFlightResponseKafkaTemplate.send(formMessage(preFlightResponse, key, from, to));
    }
}
