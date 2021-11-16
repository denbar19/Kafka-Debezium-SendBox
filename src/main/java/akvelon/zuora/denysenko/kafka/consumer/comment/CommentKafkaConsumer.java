package akvelon.denysenko.kafka.consumer.comment;

import akvelon.denysenko.handler.EventHandler;
import akvelon.denysenko.kafka.event.comment.CommentCreatedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentDeletedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentUpdatedKafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Consumes comments crud operations.
 *
 * @author Denysenko Stanislav
 */
@KafkaListener(
        topics = "${kafka.topic.json-comment}",
        groupId = "${spring.kafka.consumer.group-id}"
        , containerFactory = "kafkaListenerContainerFactory"
)
@Slf4j
@RequiredArgsConstructor
@Component
public class CommentKafkaConsumer {

    @Value("${kafka.topic.json-comment}")
    private String jsonTopic;

    private final EventHandler eventHandler;

    @KafkaHandler
    public void createCommentListener(@Payload final CommentCreatedKafkaEvent comment) {
        log.debug("Kafka createCommentListener: topic: " + jsonTopic
                + " , groupId: planningsystem. Caught message: " + comment);
        eventHandler.processEvent(comment);
    }

    @KafkaHandler
    public void updateCommentListener(@Payload final CommentUpdatedKafkaEvent comment) {
        log.debug("Kafka updateCommentListener: topic: " + jsonTopic
                + " , groupId: planningsystem. Caught message: " + comment);
        eventHandler.processEvent(comment);
    }

    @KafkaHandler
    public void deleteCommentListener(@Payload final CommentDeletedKafkaEvent comment) {
        log.debug("Kafka deleteCommentListener: topic: " + jsonTopic
                + " , groupId: planningsystem. Caught message: " + comment);
        eventHandler.processEvent(comment);
    }
}
