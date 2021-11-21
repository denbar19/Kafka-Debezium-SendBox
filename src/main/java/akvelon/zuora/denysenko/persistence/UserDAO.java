package akvelon.zuora.denysenko.persistence;

import akvelon.zuora.denysenko.entity.persistence.User;

import java.util.List;

/**
 * @author Denysenko Stanislav
 */
public interface UserDAO {

    User createUser(User user);

    List<User> getUsers(String sort, String order);

    User getUserById(long id);

    User updateUser(User user);

    User deleteUserById(long id);
}
