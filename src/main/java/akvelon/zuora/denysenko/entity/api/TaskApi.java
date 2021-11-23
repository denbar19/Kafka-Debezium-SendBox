package akvelon.zuora.denysenko.entity.api;

import akvelon.zuora.denysenko.entity.AbstractTask;
import akvelon.zuora.denysenko.entity.Priority;
import akvelon.zuora.denysenko.entity.Status;
import akvelon.zuora.denysenko.exception.TaskNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Data transfer TaskApi entity.
 *
 * @author Denysenko Stanislav
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TaskApi implements AbstractTask{

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @JsonProperty("id")
    private long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @JsonProperty("name")
    private String name;

    @Size(max = 500)
    @JsonProperty("description")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("dateAdded")
    private LocalDateTime dateAdded;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("status")
    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("priority")
    private Priority priority;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    @ToString.Exclude()
    @JsonProperty("user")
    private UserApi userApi;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    @JsonProperty("subscribers")
    private Set<UserApi> subscribersApi = new HashSet<>();

    public static void isNull(Object o) {
        if (Objects.isNull(o)) {
            throw new TaskNotFoundException("");
        }
    }
}
