package akvelon.zuora.denysenko.entity;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * EntityAction represents in Kafka messages what is the content. User in {@link akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent}.
 *
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@ToString
public enum EntityAction {
    TASK_CREATED,
    TASK_UPDATED,
    TASK_DELETED,
    TASK_REJECTED,
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED
}
