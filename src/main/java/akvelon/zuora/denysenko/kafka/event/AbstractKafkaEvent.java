package akvelon.zuora.denysenko.kafka.event;

import akvelon.zuora.denysenko.entity.EntityAction;
import akvelon.zuora.denysenko.kafka.event.task.TaskCreatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskDeletedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskRejectedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

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
        @JsonSubTypes.Type(value = UserUnsubscribedKafkaEvent.class, name = "USER_UNSUBSCRIBED")
})
public class AbstractKafkaEvent {

    protected EntityAction action;

}
