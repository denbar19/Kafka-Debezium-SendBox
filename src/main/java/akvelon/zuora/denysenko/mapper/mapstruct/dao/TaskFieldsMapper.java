package akvelon.zuora.denysenko.mapper.mapstruct.dao;

import akvelon.zuora.denysenko.entity.persistence.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 *
 * @author Denysenko Stanislav
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE
)
public interface TaskFieldsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Task mapRequestTaskToTask(Task requestTask,@MappingTarget Task taskDb);
}
