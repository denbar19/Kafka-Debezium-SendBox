package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

public class UserStatisticNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 1660106794819212503L;

    public UserStatisticNotFoundException() {
    }

    public UserStatisticNotFoundException(String message) {
        super(message);
    }
}
