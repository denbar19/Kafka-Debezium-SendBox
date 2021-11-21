package akvelon.zuora.denysenko.mapper.mapstruct.api;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Objects;

/**
 * @author Denysenko Stanislav
 */
@Mapper(componentModel = "spring", uses = {TaskApiMapper.class})
public interface UserApiMapper {

    @Mapping(target = "tasksApi", source = "tasks", qualifiedByName = "taskToTaskApi")
    @Mapping(target = "subscriptionsApi", source = "subscriptions", qualifiedByName = "subscriptionsToTaskApi")
    UserApi toUserApi(User user);

    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toUser(UserApi userApi);


    @Named("taskToTaskApi")
    static TaskApi tasksListToTaskApisList(Task task) {
        return Objects.isNull(task) ? null : TaskApi.builder()
                                                    .id(task.getId())
                                                    .name(task.getName())
                                                    .description(task.getDescription())
                                                    .dateAdded(task.getDateAdded())
                                                    .status(task.getStatus())
                                                    .priority(task.getPriority())
                                                    .userApi(null)
                                                    .subscribersApi(null)
                                                    .build();
    }

    @Named("subscriptionsToTaskApi")
    static TaskApi subscriptionsToTaskApisSubscriptions(Task task) {
        return Objects.isNull(task) ? null : TaskApi.builder()
                                                    .id(task.getId())
                                                    .name(task.getName())
                                                    .description(task.getDescription())
                                                    .dateAdded(task.getDateAdded())
                                                    .status(task.getStatus())
                                                    .priority(task.getPriority())
                                                    .userApi(null)
                                                    .subscribersApi(null)
                                                    .build();
    }
}
