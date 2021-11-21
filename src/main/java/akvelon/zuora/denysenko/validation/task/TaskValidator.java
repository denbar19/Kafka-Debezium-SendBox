package akvelon.zuora.denysenko.validation.task;

import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.validation.AbstractValidator;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class TaskValidator extends AbstractValidator<Task> {

    @Override
    public Task isNull(Task task) {
        return super.isNull(task);
    }
}
