package akvelon.denysenko.kafka.producer.user;

import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.denysenko.kafka.event.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Denysenko Stanislav
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserKafkaProducer {

    @Value("${kafka.topic.json-user}")
    private String jsonTopic;

    private final KafkaTemplate<String, AbstractKafkaEvent> kafkaTemplate;

    public void sendCreatedUserKafkaEvent(UserApi userApi) {
        UserCreatedKafkaEvent userCreatedKafkaEvent = new UserCreatedKafkaEvent(userApi);
        kafkaTemplate.send(jsonTopic, userCreatedKafkaEvent);
        log.debug("Kafka sent createUserKafkaEvent message: {}", userCreatedKafkaEvent);
    }

    public void sendUpdatedUserKafkaEvent(UserApi beforeUpdateUserApi, UserApi afterUpdateUserApi) {
        UserUpdatedKafkaEvent userUpdatedKafkaEvent = new UserUpdatedKafkaEvent(beforeUpdateUserApi, afterUpdateUserApi);
        kafkaTemplate.send(jsonTopic, userUpdatedKafkaEvent);
        log.debug("Kafka sent updateUserKafkaEvent message: {}", userUpdatedKafkaEvent);
    }

    public void sendDeletedUserKafkaEvent(UserApi userApi) {
        UserDeletedKafkaEvent userDeletedKafkaEvent = new UserDeletedKafkaEvent(userApi);
        kafkaTemplate.send(jsonTopic, userDeletedKafkaEvent);
        log.debug("Kafka sent deleteUserKafkaEvent message: {}", userDeletedKafkaEvent);
    }
}
