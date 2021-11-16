package akvelon.denysenko.kafka.event.task;

import akvelon.denysenko.entity.persistence.Comment;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.service.CommentServiceApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import java.util.Set;

import static akvelon.denysenko.entity.EntityAction.TASK_DELETED;

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
    private Set<CommentServiceApi> comments;

    public TaskDeletedKafkaEvent(final TaskApi task, final Set<CommentServiceApi> comments) {
        super(TASK_DELETED);
        this.task = task;
        this.comments = comments;
    }
}
