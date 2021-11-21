package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

public class TagNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = -8911640339660630605L;

    public TagNotFoundException(String message) {
        super(message);
    }
}
