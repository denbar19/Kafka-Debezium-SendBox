package akvelon.denysenko.kafka.serialization;

import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class AbstractKafkaEventSerializer implements Serializer<AbstractKafkaEvent> {

    public static final Logger LOG = getLogger(AbstractKafkaEventSerializer.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public byte[] serialize(String s, AbstractKafkaEvent kafkaEvent) {
        try {
            byte[] retVal = OBJECT_MAPPER.writeValueAsString(kafkaEvent).getBytes();
            LOG.debug("AbstractKafkaEventSerializer Serializer sent in bytes: " + kafkaEvent);
            return retVal;
        } catch (JsonProcessingException e) {
            LOG.warn("Can't serialize AbstractKafkaEventSerializer: " + e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
}
