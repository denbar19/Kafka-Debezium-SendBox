package akvelon.denysenko.kafka.event.comment;

import akvelon.denysenko.entity.api.CommentApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.COMMENT_UPDATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentUpdatedKafkaEvent extends AbstractKafkaEvent {

    private CommentApi updatedComment;
    private TaskApi task;
    private UserApi user;

    public CommentUpdatedKafkaEvent(final TaskApi task, final UserApi user, final CommentApi updatedComment) {
        super(COMMENT_UPDATED);
        this.updatedComment = updatedComment;
        this.task = task;
        this.user = user;
    }
}
