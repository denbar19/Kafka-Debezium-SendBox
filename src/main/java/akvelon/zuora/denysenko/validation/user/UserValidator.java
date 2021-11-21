package akvelon.zuora.denysenko.validation.user;

import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.validation.AbstractValidator;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserValidator extends AbstractValidator<User> {

    @Override
    public User isNull(User user) {
        return super.isNull(user);
    }

}
