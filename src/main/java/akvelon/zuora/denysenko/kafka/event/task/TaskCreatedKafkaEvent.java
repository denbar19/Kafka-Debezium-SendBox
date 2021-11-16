package akvelon.denysenko.kafka.event.task;

import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.TASK_CREATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskCreatedKafkaEvent extends AbstractKafkaEvent {

    private TaskApi task;

    public TaskCreatedKafkaEvent(final TaskApi task) {
        super(TASK_CREATED);
        this.task = task;
    }
}
