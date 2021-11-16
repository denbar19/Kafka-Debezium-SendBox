package akvelon.denysenko.kafka.consumer.tag;

import akvelon.denysenko.handler.EventHandler;
import akvelon.denysenko.kafka.event.tag.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes Tag crud operations.
 *
 * @author Denysenko Stanislav
 */
@KafkaListener(
        topics = "${kafka.topic.json-tag}",
        groupId = "${spring.kafka.consumer.group-id}"
        , containerFactory = "kafkaListenerContainerFactory"
)
@Slf4j
@RequiredArgsConstructor
@Component
public class TagKafkaConsumer {

    @Value("${kafka.topic.json-tag}")
    private String jsonTopic;

    private final EventHandler eventHandler;

    @KafkaHandler
    public void createdTagListener(@Payload final TagCreatedKafkaEvent tag) {
        log.debug("Kafka createTagListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + tag);
        eventHandler.processEvent(tag);
    }

    @KafkaHandler
    public void updatedTagListener(@Payload final TagUpdatedKafkaEvent tag) {
        log.debug("Kafka updateTagListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + tag);
        eventHandler.processEvent(tag);
    }

    @KafkaHandler
    public void deletedTagListener(@Payload final TagDeletedKafkaEvent tag) {
        log.debug("Kafka deleteTagListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + tag);
        eventHandler.processEvent(tag);
    }

}
