package akvelon.zuora.denysenko.mapper.mapstruct.dao;

import akvelon.zuora.denysenko.entity.persistence.User;
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
public interface UserFieldsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User mapRequestUserToUser(User requestUser,@MappingTarget User userDb);
}
