package akvelon.denysenko.kafka.event.tag;

import akvelon.denysenko.entity.api.TagApi;
import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.TaskUpdatedAction;
import akvelon.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import lombok.*;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TagAddedKafkaEvent extends TaskUpdatedKafkaEvent {

    private TagApi tag;

    public TagAddedKafkaEvent(final TaskApi beforeUpdateTaskApi, final TaskApi afterUpdateTaskApi, final TagApi tagApi) {
        super(TaskUpdatedAction.TAG_ADDED, beforeUpdateTaskApi, afterUpdateTaskApi);
        this.tag = tagApi;
    }

//    public TagAddedKafkaEvent() {
//        super(TaskUpdatedAction.TAG_ADDED);
//    }
}