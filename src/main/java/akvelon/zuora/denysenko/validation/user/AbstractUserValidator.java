package akvelon.zuora.denysenko.validation.user;

import akvelon.zuora.denysenko.validation.AbstractValidator;

public class AbstractUserValidator<AbstractUser> extends AbstractValidator<AbstractUser> {

    public AbstractUserValidator(Class<AbstractUser> clazz) {
        super(clazz);
    }

}
