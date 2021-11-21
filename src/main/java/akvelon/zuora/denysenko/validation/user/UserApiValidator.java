package akvelon.zuora.denysenko.validation.user;

public class UserApiValidator<UserApi> extends AbstractUserValidator<UserApi>{

    public UserApiValidator(Class<UserApi> clazz) {
        super(clazz);
    }

    @Override
    public UserApi isNull(UserApi userApi) {
        return super.isNull(userApi);
    }

}
