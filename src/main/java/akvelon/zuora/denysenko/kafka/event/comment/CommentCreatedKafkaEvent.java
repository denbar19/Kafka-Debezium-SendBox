package akvelon.denysenko.kafka.event.comment;

import akvelon.denysenko.entity.api.CommentApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.COMMENT_CREATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentCreatedKafkaEvent extends AbstractKafkaEvent {

    private CommentApi comment;
    private TaskApi task;
    private UserApi user;

    public CommentCreatedKafkaEvent(final TaskApi task, final UserApi user, final CommentApi comment) {
        super(COMMENT_CREATED);
        this.comment = comment;
        this.task = task;
        this.user = user;
    }
}
