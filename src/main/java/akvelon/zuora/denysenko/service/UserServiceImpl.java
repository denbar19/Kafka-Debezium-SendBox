package akvelon.zuora.denysenko.service;

import akvelon.zuora.denysenko.entity.Role;
import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.exception.ChangesNotPersistedException;
import akvelon.zuora.denysenko.exception.UserNotFoundException;
import akvelon.zuora.denysenko.kafka.producer.user.UserKafkaProducer;
import akvelon.zuora.denysenko.mapper.mapstruct.api.UserApiMapper;
import akvelon.zuora.denysenko.mapper.mapstruct.dao.UserFieldsMapper;
import akvelon.zuora.denysenko.persistence.TaskDAO;
import akvelon.zuora.denysenko.persistence.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Service layer for User Crud operations.
 *
 * @author Denysenko Stanislav
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_ORDER = "ASC";

    private final UserDAO userDAO;
    private final TaskDAO taskDAO;

    private final UserApiMapper userApiMapper;
    private final UserFieldsMapper userFieldsMapper;

    private final TaskService taskService;

    private final UserKafkaProducer userKafkaProducer;

    /**
     * Create User.
     *
     * @param user User to create
     * @return created User
     * @throws ChangesNotPersistedException if User was not created
     */
    @Override
    public User createUser(User user) throws ChangesNotPersistedException {
        if (Objects.isNull(user.getRole())) {
            user.setRole(Role.USER);
            log.debug("createUser, set default Role: {}", user);
        }

        User userResponse = userDAO.createUser(user);
        if (Objects.isNull(userResponse)) {
            log.warn("createUser, user wasn't created, request: {}", user);
            throw new ChangesNotPersistedException("User was not created, request: " + user);
        }
        log.debug("createUser, user created, response: " + userResponse);
        userKafkaProducer.sendCreatedUserKafkaEvent(userApiMapper.toUserApi(userResponse));
        return userResponse;
    }

    /**
     * Update User.
     *
     * @param user User Entity with changed fields
     * @return updated User
     * @throws UserNotFoundException        if User to update not found
     * @throws ChangesNotPersistedException if User was not updated
     */
    @Override
    public User updateUser(User user) throws UserNotFoundException, ChangesNotPersistedException {
        User userDb = userDAO.getUserById(user.getId());

        User userBeforeUpdate = userDb.toBuilder().build();

        User userUpdated = userFieldsMapper.mapRequestUserToUser(user, userDb);

        User userResponse = userDAO.updateUser(userUpdated);
        if (Objects.isNull(userResponse)) {
            log.warn("updateUser, user wasn't updated, request json: {}", userUpdated);
            throw new ChangesNotPersistedException("updateUser, user wasn't updated while saving into db, userUpdated: " + userUpdated);
        }
        log.debug("updateUser, user updated, response: {}", userResponse);
        userKafkaProducer.sendUpdatedUserKafkaEvent(
                userApiMapper.toUserApi(userBeforeUpdate), userApiMapper.toUserApi(userResponse));
        return userResponse;
    }

    /**
     * Delete User by id.
     *
     * @param id User id to delete
     * @return deleted User
     * @throws UserNotFoundException        if User to delete not found
     * @throws ChangesNotPersistedException if User was not deleted
     */
    @Override
    public User deleteUserById(long id) throws UserNotFoundException, ChangesNotPersistedException {
        User userToDelete = this.getUserById(id);
        List<Task> assignedTasks = userToDelete.getTasks();
        if (!Objects.isNull(assignedTasks)) {
            assignedTasks.forEach(taskDAO::unAssignUserFromTask);
            log.debug("deleteUserById -> user was unassigned from his tasks");
        }

        Set<Task> subscriptions = userToDelete.getSubscriptions();
        if (!Objects.isNull(subscriptions)) {
            subscriptions.forEach(task -> taskService.unSubscribeUserFromTask(task.getId(), userToDelete.getId()));
            log.debug("deleteUserById -> user was unsubscribed from his tasks");
        }

        User deletedUser = userDAO.deleteUserById(id);
        if (Objects.isNull(deletedUser)) {
            log.debug("deleteUserById, user wasn't deleted, request: {}", userToDelete);
            throw new ChangesNotPersistedException("User was not deleted, request: " + userToDelete);
        }
        log.debug("deleteUserById, user deleted, response: {}", deletedUser);
        userKafkaProducer.sendDeletedUserKafkaEvent(userApiMapper.toUserApi(deletedUser));
        return deletedUser;
    }

    /**
     * Get User by id.
     *
     * @param id task id
     * @return User by corresponding id
     * @throws UserNotFoundException if User not found in database
     */
    @Override
    public User getUserById(long id) throws UserNotFoundException {
        log.debug("getUserById: id={}", id);
        User userResponse = userDAO.getUserById(id);
        if (Objects.isNull(userResponse)) {
            log.info("getUserById, user not found: id={}", id);
            throw new UserNotFoundException("User not found: id= " + id);
        }
        log.debug("getUserById, got user by id, response: {}", userResponse);
        return userResponse;
    }

    /**
     * Get Users.
     *
     * @param sort  can sort by: id, name, surname, email
     * @param order default = ASC, DESC
     * @return List of Users y corresponding parameters
     */
    @Override
    public List<User> getUsers(String sort, String order) {
        log.debug("getUsers: sort={}, order={}", sort, order);
        List<User> userResponse = userDAO.getUsers(sort, order);
        log.debug("getUsers, got users, response: {}", userResponse);
        return userResponse;
    }

}
