package akvelon.zuora.denysenko.kafka.event.user;

import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import lombok.*;

import static akvelon.zuora.denysenko.entity.EntityAction.USER_CREATED;

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
