package akvelon.zuora.denysenko.mapper.mapstruct.api;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Objects;

/**
 * @author Denysenko Stanislav
 */
@Mapper(componentModel = "spring", uses = {UserApiMapper.class})
public interface TaskApiMapper {

    @Mapping(target = "userApi", source = "user", qualifiedByName = "userToUserApi")
    @Mapping(target = "subscribersApi", source = "subscribers")
    TaskApi toTaskApi(Task task);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Task toTask(TaskApi taskApi);

    @Named("userToUserApi")
    static UserApi userToUserApi(User user) {
        return Objects.isNull(user) ? null : UserApi.builder()
                                                    .id(user.getId())
                                                    .name(user.getName())
                                                    .surname(user.getSurname())
                                                    .email(user.getEmail())
                                                    .dateAdded(user.getDateAdded())
                                                    .role(user.getRole())
                                                    .tasksApi(Collections.emptySet())
                                                    .build();
    }

}
