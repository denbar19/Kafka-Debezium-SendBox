package akvelon.denysenko.kafka.event.task;

import akvelon.denysenko.entity.persistence.Comment;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.service.CommentServiceApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import java.util.Set;

import static akvelon.denysenko.entity.EntityAction.TASK_REJECTED;


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
    private Set<CommentServiceApi> comments;

    public TaskRejectedKafkaEvent(TaskApi task, final Set<CommentServiceApi> comments) {
        super(TASK_REJECTED);
        this.task = task;
        this.comments = comments;
    }

}
