package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Denysenko Stanislav
 */
public class TaskNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = -5597084406679515647L;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
