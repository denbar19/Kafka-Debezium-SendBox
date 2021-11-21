package akvelon.zuora.denysenko.kafka.event.task;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.entity.TaskUpdatedAction;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.zuora.denysenko.entity.EntityAction.TASK_UPDATED;


/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskUpdatedKafkaEvent extends AbstractKafkaEvent {

    private TaskUpdatedAction taskUpdatedAction;
    private TaskApi beforeUpdateTask;
    private TaskApi afterUpdateTask;

    public TaskUpdatedKafkaEvent(TaskUpdatedAction taskUpdatedAction, final TaskApi beforeUpdateTask, final TaskApi afterUpdateTask) {
        super(TASK_UPDATED);
        this.taskUpdatedAction = taskUpdatedAction;
        this.beforeUpdateTask = beforeUpdateTask;
        this.afterUpdateTask = afterUpdateTask;
    }

    public TaskUpdatedKafkaEvent(final TaskApi beforeUpdateTask, final TaskApi afterUpdateTask) {
        super(TASK_UPDATED);
        this.taskUpdatedAction = TaskUpdatedAction.TASK_UPDATED;
        this.beforeUpdateTask = beforeUpdateTask;
        this.afterUpdateTask = afterUpdateTask;
    }

    public TaskUpdatedKafkaEvent(TaskUpdatedAction taskUpdatedAction) {
        super(TASK_UPDATED);
        this.taskUpdatedAction = taskUpdatedAction;
    }

}
