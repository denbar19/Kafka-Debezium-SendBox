package akvelon.denysenko.kafka.consumer.user;

import akvelon.denysenko.handler.EventHandler;
import akvelon.denysenko.kafka.event.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes User crud operations.
 *
 * @author Denysenko Stanislav
 */
@KafkaListener(
        topics = "${kafka.topic.json-user}",
        groupId = "${spring.kafka.consumer.group-id}"
        , containerFactory = "kafkaListenerContainerFactory"
)
@Slf4j
@RequiredArgsConstructor
@Component
public class UserKafkaConsumer {

    @Value("${kafka.topic.json-user}")
    private String jsonTopic;

    private final EventHandler eventHandler;

    @KafkaHandler
    public void createUserListener(@Payload final UserCreatedKafkaEvent user) {
        log.debug("Kafka createTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

    @KafkaHandler
    public void updateUserListener(@Payload final UserUpdatedKafkaEvent user) {
        log.debug("Kafka updateTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

    @KafkaHandler
    public void deleteUserListener(@Payload final UserDeletedKafkaEvent user) {
        log.debug("Kafka deleteTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

}
