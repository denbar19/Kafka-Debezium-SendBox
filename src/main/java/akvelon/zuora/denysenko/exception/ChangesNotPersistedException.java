package akvelon.zuora.denysenko.exception;

public class ChangesNotPersistedException extends RuntimeException{

    private static final long serialVersionUID = 3872791840073414552L;

    public ChangesNotPersistedException(String message) {
        super(message);
    }
}
