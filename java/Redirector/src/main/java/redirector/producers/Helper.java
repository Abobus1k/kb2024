package redirector.producers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;

@Slf4j
public abstract class Helper {
    protected static <T> ProducerRecord<String, T> formMessage(final T event, final String key, final String from, final String to) {
        var rec = new ProducerRecord<>(
                "monitor",
                key,
                event
        );
        log.info("rec ravno = " + rec.toString());


        rec.headers().add("from", from.getBytes());
        rec.headers().add("to", to.getBytes());

        log.info("headers ravno = " + rec.headers().toString());
        return rec;
    }
}
