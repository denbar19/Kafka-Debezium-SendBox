package akvelon.zuora.denysenko.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadRequestException extends RuntimeException{

    private static final long serialVersionUID = -4910103554401334085L;

    public BadRequestException(String message) {
        super(message);
    }
}
