package akvelon.zuora.denysenko.kafka.event.task;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.zuora.denysenko.entity.EntityAction.TASK_DELETED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskDeletedKafkaEvent extends AbstractKafkaEvent {

    private TaskApi task;

    public TaskDeletedKafkaEvent(final TaskApi task) {
        super(TASK_DELETED);
        this.task = task;
    }
}
