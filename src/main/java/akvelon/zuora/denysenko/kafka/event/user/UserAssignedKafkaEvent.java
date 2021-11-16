package akvelon.denysenko.kafka.event.user;

import akvelon.denysenko.entity.api.TaskApi;
import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.TaskUpdatedAction.USER_ASSIGNED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserAssignedKafkaEvent extends TaskUpdatedKafkaEvent {

    private UserApi user;

    public UserAssignedKafkaEvent(final TaskApi beforeUpdateTask, final TaskApi afterUpdateTask, final UserApi userApi) {
        super(USER_ASSIGNED, beforeUpdateTask, afterUpdateTask);
        this.user = userApi;
    }

}
