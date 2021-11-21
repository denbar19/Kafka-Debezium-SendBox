package akvelon.zuora.denysenko.validation;

import akvelon.zuora.denysenko.exception.validation.ValidationExceptionImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AbstractValidator<T> {

    private final Class<T> clazz;

    public T isNull(T t) {
        if (Objects.isNull(t)) {
            log.warn(clazz + " object is null");
            throw new ValidationExceptionImpl(clazz + " object is null");
        }
        return (T)t;
    }
}
