package akvelon.denysenko.kafka.event;

import akvelon.denysenko.entity.EntityAction;
import akvelon.denysenko.kafka.event.comment.CommentCreatedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentDeletedKafkaEvent;
import akvelon.denysenko.kafka.event.comment.CommentUpdatedKafkaEvent;
import akvelon.denysenko.kafka.event.tag.*;
import akvelon.denysenko.kafka.event.task.TaskCreatedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskDeletedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskRejectedKafkaEvent;
import akvelon.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import akvelon.denysenko.kafka.event.user.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static akvelon.denysenko.entity.EntityAction.*;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "action")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TaskCreatedKafkaEvent.class, name = "TASK_CREATED"),
        @JsonSubTypes.Type(value = TaskUpdatedKafkaEvent.class, name = "TASK_UPDATED"),
        @JsonSubTypes.Type(value = TaskDeletedKafkaEvent.class, name = "TASK_DELETED"),
        @JsonSubTypes.Type(value = TaskRejectedKafkaEvent.class, name = "TASK_REJECTED"),
        @JsonSubTypes.Type(value = UserCreatedKafkaEvent.class, name = "USER_CREATED"),
        @JsonSubTypes.Type(value = UserUpdatedKafkaEvent.class, name = "USER_UPDATED"),
        @JsonSubTypes.Type(value = UserDeletedKafkaEvent.class, name = "USER_DELETED"),

        @JsonSubTypes.Type(value = UserAssignedKafkaEvent.class, name = "USER_ASSIGNED"),
        @JsonSubTypes.Type(value = UserUnassignedKafkaEvent.class, name = "USER_UNASSIGNED"),
        @JsonSubTypes.Type(value = UserSubscribedKafkaEvent.class, name = "USER_SUBSCRIBED"),
        @JsonSubTypes.Type(value = UserUnsubscribedKafkaEvent.class, name = "USER_UNSUBSCRIBED"),

        @JsonSubTypes.Type(value = CommentCreatedKafkaEvent.class, name = "COMMENT_CREATED"),
        @JsonSubTypes.Type(value = CommentUpdatedKafkaEvent.class, name = "COMMENT_UPDATED"),
        @JsonSubTypes.Type(value = CommentDeletedKafkaEvent.class, name = "COMMENT_DELETED"),
        @JsonSubTypes.Type(value = TagCreatedKafkaEvent.class, name = "TAG_CREATED"),
        @JsonSubTypes.Type(value = TagUpdatedKafkaEvent.class, name = "TAG_UPDATED"),
        @JsonSubTypes.Type(value = TagDeletedKafkaEvent.class, name = "TAG_DELETED"),

        @JsonSubTypes.Type(value = TagAddedKafkaEvent.class, name = "TAG_ADDED"),
        @JsonSubTypes.Type(value = TagRemovedKafkaEvent.class, name = "TAG_REMOVED")
})
public class AbstractKafkaEvent {

    protected EntityAction action;

}
