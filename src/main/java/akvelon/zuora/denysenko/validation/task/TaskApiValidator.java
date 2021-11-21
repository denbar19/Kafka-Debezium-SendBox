package akvelon.zuora.denysenko.validation.task;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.validation.AbstractValidator;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class TaskApiValidator extends AbstractValidator<TaskApi> {

    @Override
    public TaskApi isNull(TaskApi taskApi) {
        return super.isNull(taskApi);
    }
}
