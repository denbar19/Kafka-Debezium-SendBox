package akvelon.zuora.denysenko.validation;

import akvelon.zuora.denysenko.exception.validation.ValidationExceptionImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AbstractValidator<T> {

    public T isNull(T t) {
        if (Objects.isNull(t)) {
            throw new ValidationExceptionImpl("Object is null");
        }
        return (T)t;
    }
}
