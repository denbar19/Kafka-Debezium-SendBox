package akvelon.zuora.denysenko.kafka.event.task;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.zuora.denysenko.entity.EntityAction.TASK_REJECTED;


/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskRejectedKafkaEvent extends AbstractKafkaEvent {

    private TaskApi task;

    public TaskRejectedKafkaEvent(TaskApi task) {
        super(TASK_REJECTED);
        this.task = task;
    }

}
