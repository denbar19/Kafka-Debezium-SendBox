package akvelon.denysenko.kafka.event.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.TAG_UPDATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TagUpdatedKafkaEvent extends AbstractKafkaEvent {

    private TagApi beforeUpdateTag;
    private TagApi afterUpdateTag;

    public TagUpdatedKafkaEvent(final TagApi beforeUpdateTag, final TagApi afterUpdateTag) {
        super(TAG_UPDATED);
        this.beforeUpdateTag = beforeUpdateTag;
        this.afterUpdateTag = afterUpdateTag;
    }

}
