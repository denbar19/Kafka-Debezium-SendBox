package akvelon.zuora.denysenko.exception;

import javax.persistence.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    private static final long serialVersionUID = 2811502031162535654L;

    public CommentNotFoundException(String message) {
        super(message);
    }
}
