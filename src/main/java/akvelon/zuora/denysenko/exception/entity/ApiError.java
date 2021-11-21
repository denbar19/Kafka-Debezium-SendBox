package akvelon.zuora.denysenko.exception.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Denysenko Stanislav
 */
@Data
@RequiredArgsConstructor
public class ApiError {

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

}
