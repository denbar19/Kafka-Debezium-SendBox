package akvelon.zuora.denysenko.validation.task;

public class TaskApiValidator<TaskApi> extends AbstractTaskValidator<TaskApi>{

    public TaskApiValidator(Class<TaskApi> clazz) {
        super(clazz);
    }

    @Override
    public TaskApi isNull(TaskApi taskApi) {
        return super.isNull(taskApi);
    }
}
