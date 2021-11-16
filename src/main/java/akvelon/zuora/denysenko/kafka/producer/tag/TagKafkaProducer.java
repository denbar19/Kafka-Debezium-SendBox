package akvelon.denysenko.kafka.producer.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.denysenko.kafka.event.tag.*;
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
public class TagKafkaProducer {

    @Value("${kafka.topic.json-tag}")
    private String jsonTopic;

    private final KafkaTemplate<String, AbstractKafkaEvent> kafkaTemplate;

    public void sendCreatedTagKafkaEvent(TagApi tagApi) {
        TagCreatedKafkaEvent tagCreatedKafkaEvent = new TagCreatedKafkaEvent(tagApi);
        kafkaTemplate.send(jsonTopic, tagCreatedKafkaEvent);
        log.debug("sendCreateTagKafkaEvent, kafka sent tagCreatedKafkaEvent message: {}", tagCreatedKafkaEvent);
    }

    public void sendUpdatedTagKafkaEvent(TagApi beforeUpdateTagApi, TagApi afterUpdateTagApi) {
        TagUpdatedKafkaEvent tagUpdatedKafkaEvent = new TagUpdatedKafkaEvent(beforeUpdateTagApi, afterUpdateTagApi);
        kafkaTemplate.send(jsonTopic, tagUpdatedKafkaEvent);
        log.debug("sendUpdateTagKafkaEvent, kafka sent tagUpdatedKafkaEvent message: {}", tagUpdatedKafkaEvent);
    }

    public void sendDeletedTagKafkaEvent(TagApi tagApi) {
        TagDeletedKafkaEvent tagDeletedKafkaEvent = new TagDeletedKafkaEvent(tagApi);
        kafkaTemplate.send(jsonTopic, tagDeletedKafkaEvent);
        log.debug("sendDeleteTagKafkaEvent, kafka sent tagDeletedKafkaEvent message: {}", tagDeletedKafkaEvent);
    }


}
