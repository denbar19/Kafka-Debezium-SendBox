package akvelon.zuora.denysenko.validation.user;

import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.validation.AbstractValidator;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserApiValidator extends AbstractValidator<UserApi> {

    @Override
    public UserApi isNull(UserApi userApi) {
        return super.isNull(userApi);
    }

}
