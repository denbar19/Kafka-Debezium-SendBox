package akvelon.zuora.denysenko.entity.api;

import akvelon.zuora.denysenko.entity.AbstractUser;
import akvelon.zuora.denysenko.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data transfer UserApi entity.
 *
 * @author Denysenko Stanislav
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserApi implements AbstractUser {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    @JsonProperty("id")
    private long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @JsonProperty("name")
    private String name;

    @NotBlank
    @Size(min = 2, max = 100)
    @JsonProperty("surname")
    private String surname;

    @Email
    @NotBlank
    @JsonProperty("email")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonProperty("dateAdded")
    private LocalDateTime dateAdded;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("role")
    private Role role;

    @EqualsAndHashCode.Exclude
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    @JsonProperty("tasks")
    private Set<TaskApi> tasksApi;

    @EqualsAndHashCode.Exclude
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
    @JsonProperty("subscriptions")
    private Set<TaskApi> subscriptionsApi;

    @JsonIgnore
    public String getNameSurname() {
        return surname + " " + name;
    }
}
