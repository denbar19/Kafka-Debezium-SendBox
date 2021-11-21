package akvelon.zuora.denysenko.entity;

import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * TaskUpdatedAction represents task update events that doesn't connected to basic CRUD operations and flying kafka events.
 * used in {@link akvelon.denysenko.handler.EventHandler} class, {@code taskUpdateEventsOptions} map.
 *
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@ToString
public enum TaskUpdatedAction {
    TASK_UPDATED,
    TAG_ADDED,
    TAG_REMOVED,
    USER_ASSIGNED,
    USER_UNASSIGNED,
    USER_SUBSCRIBED,
    USER_UNSUBSCRIBED
}
