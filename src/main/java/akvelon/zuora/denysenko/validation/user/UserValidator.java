package akvelon.zuora.denysenko.validation.user;

public class UserValidator<User> extends AbstractUserValidator<User> {

    public UserValidator(Class<User> clazz) {
        super(clazz);
    }

    @Override
    public User isNull(User user) {
        return super.isNull(user);
    }

}
