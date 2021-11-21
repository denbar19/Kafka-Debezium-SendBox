package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = -1180115980585571303L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
