package akvelon.denysenko.kafka.producer.task;

import akvelon.denysenko.entity.persistence.Comment;
import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.entity.service.CommentServiceApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.denysenko.kafka.event.tag.TagAddedKafkaEvent;
import akvelon.denysenko.kafka.event.tag.TagRemovedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskCreatedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskDeletedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskRejectedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import akvelon.denysenko.kafka.event.user.UserAssignedKafkaEvent;
import akvelon.denysenko.kafka.event.user.UserSubscribedKafkaEvent;
import akvelon.denysenko.kafka.event.user.UserUnassignedKafkaEvent;
import akvelon.denysenko.kafka.event.user.UserUnsubscribedKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public void sendDeletedTaskKafkaEvent(TaskApi taskApi, Set<CommentServiceApi> comments) {
        TaskDeletedKafkaEvent taskDeletedKafkaEvent = new TaskDeletedKafkaEvent(taskApi, comments);
        kafkaTemplate.send(jsonTopic, taskDeletedKafkaEvent);
        log.debug("sendDeleteTaskKafkaEvent, kafka sent deleteTaskKafkaEvent message: {}", taskDeletedKafkaEvent);
    }

    public void sendRejectedTaskKafkaEvent(TaskApi taskApi, Set<CommentServiceApi> comments) {
        TaskRejectedKafkaEvent taskRejectKafkaEvent = new TaskRejectedKafkaEvent(taskApi, comments);
        kafkaTemplate.send(jsonTopic, taskRejectKafkaEvent);
        log.debug("sendRejectTaskKafkaEvent, kafka sent rejectedTaskKafkaEvent message: {}", taskRejectKafkaEvent);
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

    public void sendTagAddedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, TagApi tagApi) {
        TagAddedKafkaEvent tagAddedKafkaEvent = new TagAddedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, tagApi);
        kafkaTemplate.send(jsonTopic, tagAddedKafkaEvent);
        log.debug("sendTagAddedKafkaEvent, kafka sent tagAddedKafkaEvent message: {}", tagAddedKafkaEvent);
    }

    public void sendTagRemovedKafkaEvent(TaskApi beforeUpdateTaskApi, TaskApi afterUpdateTaskApi, TagApi tagApi) {
        TagRemovedKafkaEvent tagRemovedKafkaEvent = new TagRemovedKafkaEvent(beforeUpdateTaskApi, afterUpdateTaskApi, tagApi);
        kafkaTemplate.send(jsonTopic, tagRemovedKafkaEvent);
        log.debug("sendTagRemovedKafkaEvent, kafka sent tagRemovedKafkaEvent message: {}", tagRemovedKafkaEvent);
    }
}
