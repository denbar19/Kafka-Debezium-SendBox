package akvelon.denysenko.kafka.event.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.TAG_CREATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TagCreatedKafkaEvent extends AbstractKafkaEvent {

    private TagApi tagApi;

    public TagCreatedKafkaEvent(final TagApi tagApi) {
        super(TAG_CREATED);
        this.tagApi = tagApi;
    }
}
