package akvelon.denysenko.kafka.event.comment;

import akvelon.denysenko.entity.api.CommentApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.COMMENT_DELETED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentDeletedKafkaEvent extends AbstractKafkaEvent {

    private CommentApi commentApi;

    public CommentDeletedKafkaEvent(final CommentApi commentApi) {
        super(COMMENT_DELETED);
        this.commentApi = commentApi;
    }
}
