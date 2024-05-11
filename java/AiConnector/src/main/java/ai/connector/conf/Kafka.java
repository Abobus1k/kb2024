package ai.connector.conf;

import ai.connector.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Configuration
public class Kafka {
    public static final String MISSION_PREPARE_REQUEST = "missionPrepareRequest";
    public static final String CREATE_AI_CONNECTION = "createAiConnection";
    public static final String MISSION_INFO_RESPONSE = "missionInfoResponse";
    public static final String PREFLIGHT_RESPONSE = "preflightResponse";
    private final KafkaProperties properties;

    public Kafka(KafkaProperties properties) {
        Objects.requireNonNull(properties, "KafkaProperties must not be null");
        this.properties = properties;
    }

    @Bean(CREATE_AI_CONNECTION)
    public KafkaTemplate<String, Connection> createAiConnection() {
        return new KafkaTemplate<>(producerFactory(
                properties -> {
                    properties.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        ));
    }

    @Bean(MISSION_PREPARE_REQUEST)
    public KafkaTemplate<String, MissionPrepareRequest> missionPrepareRequest() {
        return new KafkaTemplate<>(producerFactory(
                properties -> {
                    properties.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        ));
    }

    @Bean(MISSION_INFO_RESPONSE)
    public KafkaTemplate<String, MissionInfoResponse> missionInfoResponseKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(
                properties -> {
                    properties.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        ));
    }

    @Bean(PREFLIGHT_RESPONSE)
    public KafkaTemplate<String, PreFlightResponse> preflightResponseKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory(
                properties -> {
                    properties.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        ));
    }

    private <T> ProducerFactory<String, T> producerFactory(
            final Consumer<Map<String, Object>> enchanter
    ) {
        var props = properties.buildProducerProperties(null);
        // Работаем со строками
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        // Партиция одна, так что все равно как роутить
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                RoundRobinPartitioner.class);
        // Отправляем сообщения сразу
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        // До-обогащаем конфигурацию
        enchanter.accept(props);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Connection>
    createConnection() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, Connection>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Connection.class)
                )
        );
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MissionInfoRequest>
    missionInfoRequestContainerFactory() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, MissionInfoRequest>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(MissionInfoRequest.class)
                )
        );
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PreFlightRequest>
    preFlightRequestContainerFactory() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, PreFlightRequest>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(PreFlightRequest.class)
                )
        );
        return factory;
    }
}