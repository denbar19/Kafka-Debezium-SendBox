package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

public class UnableToDeleteTask extends EntityNotFoundException {

    private static final long serialVersionUID = -6123819630473717574L;

    public UnableToDeleteTask(String message) {
        super(message);
    }
}
