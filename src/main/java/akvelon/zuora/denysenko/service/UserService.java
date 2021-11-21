package akvelon.zuora.denysenko.service;

import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.exception.ChangesNotPersistedException;
import akvelon.zuora.denysenko.exception.UserNotFoundException;

import java.util.List;

/**
 * CRUD operations on the Users
 *
 * @author Denysenko Stanislav
 */
public interface UserService {

    /**
     * Creates new User.
     *
     * @param user User Entity with desired fields
     * @return created User Entity
     */
    User createUser(User user);


    /**
     * Updates task content.
     *
     * @param user User Entity with changed fields
     * @return updated User entity
     */
    User updateUser(User user) throws UserNotFoundException, ChangesNotPersistedException;

    /**
     * Delete User with given id.
     *
     * @param id requested id of the task
     * @return User Entity of this id
     */
    User deleteUserById(long id) throws UserNotFoundException;

    /**
     * Get Users List.
     *
     * @param sort  Name of sorting filed: ID,
     * @param order default = ASC, DESC
     * @return List of all users
     */
    List<User> getUsers(String sort, String order);

    /**
     * Get User with given id.
     *
     * @param id task id
     * @return User Entity of this id
     */
    User getUserById(long id) throws UserNotFoundException;

}
