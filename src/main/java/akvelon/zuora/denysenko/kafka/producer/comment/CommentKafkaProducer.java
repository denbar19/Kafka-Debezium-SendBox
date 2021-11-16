package akvelon.zuora.denysenko.kafka.producer.comment;

import akvelon.denysenko.entity.api.CommentApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentCreatedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentDeletedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentUpdatedKafkaEvent;
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
public class CommentKafkaProducer {

    @Value("${kafka.topic.json-comment}")
    private String jsonTopic;

    private final KafkaTemplate<String, AbstractKafkaEvent> kafkaTemplate;

    public void sendCreatedCommentKafkaEvent(TaskApi taskApi, UserApi userApi, CommentApi commentApi) {
        CommentCreatedKafkaEvent commentCreatedKafkaEvent = new CommentCreatedKafkaEvent(taskApi, userApi, commentApi);
        kafkaTemplate.send(jsonTopic, commentCreatedKafkaEvent);
        log.debug("sendCreateCommentKafkaEvent, kafka sent commentCreatedKafkaEvent message: {}", commentCreatedKafkaEvent);
    }

    public void sendUpdatedCommentKafkaEvent(TaskApi taskApi, UserApi userApi, CommentApi afterUpdateCommentApi) {
        CommentUpdatedKafkaEvent commentUpdatedKafkaEvent = new CommentUpdatedKafkaEvent(taskApi, userApi, afterUpdateCommentApi);
        kafkaTemplate.send(jsonTopic, commentUpdatedKafkaEvent);
        log.debug("sendUpdateCommentKafkaEvent, kafka sent commentUpdatedKafkaEvent message: {}", commentUpdatedKafkaEvent);
    }

    public void sendDeletedCommentKafkaEvent(CommentApi commentApi) {
        CommentDeletedKafkaEvent commentDeletedKafkaEvent = new CommentDeletedKafkaEvent(commentApi);
        kafkaTemplate.send(jsonTopic, commentDeletedKafkaEvent);
        log.debug("sendDeleteCommentKafkaEvent, kafka sent commentDeletedKafkaEvent message: {}", commentDeletedKafkaEvent);
    }
}
