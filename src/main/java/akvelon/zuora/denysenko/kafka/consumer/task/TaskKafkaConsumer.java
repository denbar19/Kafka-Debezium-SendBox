package akvelon.zuora.denysenko.kafka.consumer.task;

import akvelon.zuora.denysenko.handler.EventHandler;
import akvelon.zuora.denysenko.kafka.event.task.TaskCreatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskDeletedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.UserAssignedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.UserSubscribedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.UserUnassignedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.UserUnsubscribedKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes Task operations.
 *
 * @author Denysenko Stanislav
 */
@KafkaListener(
        topics = "${kafka.topic.json-task}",
        groupId = "${spring.kafka.consumer.group-id}"
        , containerFactory = "kafkaListenerContainerFactory"
)
@Slf4j
@RequiredArgsConstructor
@Component
public class TaskKafkaConsumer {

    @Value("${kafka.topic.json-task}")
    private String jsonTopic;

    private final EventHandler eventHandler;

    @KafkaHandler
    public void createTaskListener(@Payload final TaskCreatedKafkaEvent task) {
        log.debug("Kafka createTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + task);
        eventHandler.processEvent(task);
    }

    @KafkaHandler
    public void updateTaskListener(@Payload final TaskUpdatedKafkaEvent task) {
        log.debug("Kafka updateTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + task);
        eventHandler.processEvent(task);
    }

    @KafkaHandler
    public void deleteTaskListener(@Payload final TaskDeletedKafkaEvent task) {
        log.debug("Kafka deleteTaskListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + task);
        eventHandler.processEvent(task);
    }

    @KafkaHandler
    public void assignUserListener(@Payload final UserAssignedKafkaEvent user) {
        log.debug("Kafka assignUserListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

    @KafkaHandler
    public void unassignUserListener(@Payload final UserUnassignedKafkaEvent user) {
        log.debug("Kafka unassignUserListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

    @KafkaHandler
    public void userSubscribeListener(@Payload final UserSubscribedKafkaEvent user) {
        log.debug("Kafka userSubscribeListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

    @KafkaHandler
    public void userUnsubscribeListener(@Payload final UserUnsubscribedKafkaEvent user) {
        log.debug("Kafka userUnsubscribeListener: topic: " + jsonTopic + " , groupId: planningsystem. Caught message: " + user);
        eventHandler.processEvent(user);
    }

}
