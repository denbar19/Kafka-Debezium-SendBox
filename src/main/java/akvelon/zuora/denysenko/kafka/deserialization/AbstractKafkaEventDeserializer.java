package akvelon.zuora.denysenko.kafka.deserialization;

import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class AbstractKafkaEventDeserializer implements Deserializer<AbstractKafkaEvent> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public AbstractKafkaEvent deserialize(String s, byte[] bytes) {
        try {
            var kafkaEvent = OBJECT_MAPPER.readValue(bytes, AbstractKafkaEvent.class);
            log.debug("AbstractKafkaEvent Deserializer received: " + kafkaEvent);
            return kafkaEvent;
        } catch (JsonParseException exception) {
            log.error("Can't parse AbstractKafkaEvent: " + exception.getMessage() + Arrays.toString(exception.getStackTrace()));
        } catch (JsonMappingException exception) {
            log.error("Can't map AbstractKafkaEvent: " + exception.getMessage() + Arrays.toString(exception.getStackTrace()));
        } catch (IOException exception) {
            log.error("Failed in deserialize AbstractKafkaEvent: " + exception.getMessage() + Arrays.toString(exception.getStackTrace()));
        }
        return null;
    }
}
