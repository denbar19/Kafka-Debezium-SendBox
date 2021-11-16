package akvelon.denysenko.kafka.event.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.TaskUpdatedAction;
import akvelon.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Denysenko Stanislav
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TagRemovedKafkaEvent extends TaskUpdatedKafkaEvent {

    private TagApi tag;

    public TagRemovedKafkaEvent(final TaskApi beforeUpdateTaskApi, final TaskApi afterUpdateTaskApi, final TagApi tagApi) {
        super(TaskUpdatedAction.TAG_REMOVED, beforeUpdateTaskApi, afterUpdateTaskApi);
        this.tag = tagApi;
    }

    public TagRemovedKafkaEvent() {
        super(TaskUpdatedAction.TAG_REMOVED);
    }
}
