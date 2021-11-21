package akvelon.zuora.denysenko.validation.task;

import akvelon.zuora.denysenko.validation.AbstractValidator;

public class AbstractTaskValidator<AbstractTask> extends AbstractValidator<AbstractTask> {

    public AbstractTaskValidator(Class<AbstractTask> clazz) {
        super(clazz);
    }

}
