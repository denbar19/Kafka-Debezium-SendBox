package akvelon.zuora.denysenko.validation.task;

public class TaskValidator<Task> extends AbstractTaskValidator<Task>{

    public TaskValidator(Class<Task> clazz) {
        super(clazz);
    }

}
