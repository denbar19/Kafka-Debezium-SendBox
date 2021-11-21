package akvelon.zuora.denysenko.kafka.event.user;

import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.zuora.denysenko.entity.EntityAction.USER_UPDATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserUpdatedKafkaEvent extends AbstractKafkaEvent {

    protected UserApi beforeUpdateUser;
    protected UserApi afterUpdateUser;

    public UserUpdatedKafkaEvent(final UserApi beforeUpdateUser, final UserApi afterUpdateUser) {
        super(USER_UPDATED);
        this.beforeUpdateUser = beforeUpdateUser;
        this.afterUpdateUser = afterUpdateUser;
    }
}
