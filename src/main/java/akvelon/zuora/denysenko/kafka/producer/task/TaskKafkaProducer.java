package akvelon.zuora.denysenko.kafka.producer.task;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Denysenko Stanislav
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskKafkaProducer {

    @Value("${kafka.topic.json-task}")
    private String jsonTopic;

    private final KafkaTemplate<String, AbstractKafkaEvent> kafkaTemplate;

    // Basic task Events

    public void sendCreatedTaskKafkaEvent(TaskApi taskApi) {
        TaskCreatedKafkaEvent taskCreatedKafkaEvent = new TaskCreatedKafkaEvent(taskApi);
        kafkaTemplate.send(jsonTopic, taskCreatedKafkaEvent);
        log.debug("sendCreateTaskKafkaEvent, kafka sent createTaskKafkaEvent message: {}", taskCreatedKafkaEvent);
    }

    public void sendUpdatedTaskKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi) {
        TaskUpdatedKafkaEvent taskUpdatedKafkaEvent = new TaskUpdatedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi);
        kafkaTemplate.send(jsonTopic, taskUpdatedKafkaEvent);
        log.debug("sendUpdateTaskKafkaEvent, kafka sent updateUTaskKafkaEvent message: {}", taskUpdatedKafkaEvent);
    }

    public void sendDeletedTaskKafkaEvent(TaskApi taskApi) {
        TaskDeletedKafkaEvent taskDeletedKafkaEvent = new TaskDeletedKafkaEvent(taskApi);
        kafkaTemplate.send(jsonTopic, taskDeletedKafkaEvent);
        log.debug("sendDeleteTaskKafkaEvent, kafka sent deleteTaskKafkaEvent message: {}", taskDeletedKafkaEvent);
    }

    // Task updates for Tags, Comments, User, Subscribers

    public void sendUserAssignedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, UserApi userApi) {
        UserAssignedKafkaEvent userAssignedKafkaEvent = new UserAssignedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, userApi);
        kafkaTemplate.send(jsonTopic, userAssignedKafkaEvent);
        log.debug("Kafka sent userAssignedKafkaEvent message: {}", userAssignedKafkaEvent);
    }

    public void sendUserUnassignedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, UserApi userApi) {
        UserUnassignedKafkaEvent userUnassignedKafkaEvent = new UserUnassignedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, userApi);
        kafkaTemplate.send(jsonTopic, userUnassignedKafkaEvent);
        log.debug("Kafka sent userUnassignedKafkaEvent message: {}", userUnassignedKafkaEvent);
    }

    public void sendUserSubscribedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, UserApi userApi) {
        UserSubscribedKafkaEvent userSubscribedKafkaEvent = new UserSubscribedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, userApi);
        kafkaTemplate.send(jsonTopic, userSubscribedKafkaEvent);
        log.debug("Kafka sent userSubscribedKafkaEvent message: {}", userSubscribedKafkaEvent);
    }

    public void sendUserUnsubscribedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, UserApi userApi) {
        UserUnsubscribedKafkaEvent userUnsubscribedKafkaEvent = new UserUnsubscribedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, userApi);
        kafkaTemplate.send(jsonTopic, userUnsubscribedKafkaEvent);
        log.debug("Kafka sent userUnsubscribedKafkaEvent message: {}", userUnsubscribedKafkaEvent);
    }

}
