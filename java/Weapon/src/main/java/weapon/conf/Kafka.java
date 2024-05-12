package weapon.conf;

import org.apache.kafka.clients.consumer.ConsumerConfig;
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
import weapon.Message;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Configuration
public class Kafka {
    public static final String SEND_MISSION_INFO_REQUEST = "sendMissionInfoRequest";
    public static final String PRE_FLIGHT_REQUEST = "preFlightRequest";
    public static final String READY_FOR_MISSION = "readyForMission";
    public static final String FLIGHT_INFO_REQUEST = "flightInfoRequest";
    public static final String WEAPON_RESPONSE = "weaponResponse";
    public static final String MESSAGE = "message";
    private final KafkaProperties properties;

    public Kafka(KafkaProperties properties) {
        Objects.requireNonNull(properties, "KafkaProperties must not be null");
        this.properties = properties;
    }

    @Bean(MESSAGE)
    public KafkaTemplate<String, Message> message() {
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
    public ConcurrentKafkaListenerContainerFactory<String, Message>
    messageContainerFactory() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, Message>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Message.class)
                )
        );
        return factory;
    }
}