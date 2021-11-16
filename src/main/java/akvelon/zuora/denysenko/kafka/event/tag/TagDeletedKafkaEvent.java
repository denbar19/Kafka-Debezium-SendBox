package akvelon.denysenko.kafka.event.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.TAG_DELETED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TagDeletedKafkaEvent extends AbstractKafkaEvent {

    private TagApi tagApi;

    public TagDeletedKafkaEvent(final TagApi tagApi) {
        super(TAG_DELETED);
        this.tagApi = tagApi;
    }
}
