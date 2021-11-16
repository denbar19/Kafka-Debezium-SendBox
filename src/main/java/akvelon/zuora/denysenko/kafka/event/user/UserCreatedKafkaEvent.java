package akvelon.denysenko.kafka.event.user;

import akvelon.denysenko.entity.api.UserApi;
import akvelon.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.denysenko.entity.EntityAction.USER_CREATED;

/**
 * @author Denysenko Stanislav
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserCreatedKafkaEvent extends AbstractKafkaEvent {

    protected UserApi user;

    public UserCreatedKafkaEvent(final UserApi user) {
        super(USER_CREATED);
        this.user = user;
    }
}
