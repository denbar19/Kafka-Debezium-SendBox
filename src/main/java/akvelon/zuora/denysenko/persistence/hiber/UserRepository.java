package akvelon.zuora.denysenko.persistence.hiber;

import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.persistence.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static java.lang.String.format;

/**
 * @author Denysenko Stanislav
 */
@Slf4j
@Repository("UserRepository")
public class UserRepository extends RepositoryImpl<User> implements UserDAO {

    @Autowired
    public UserRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, User.class);
    }

    @Override
    public User createUser(User user) {
        log.debug("createUser, user create, call super");
        return super.create(user);
    }

    @Override
    public List<User> getUsers(String sort, String order) {
        log.debug(format("getUsers, get users: sort=%s, order=%s, call super", sort, order));
        return super.getList(sort, order);
    }

    @Override
    public User getUserById(long id) {
        log.debug(format("getUserById, get user by id=%d, call super", id));
        return super.getById(id);
    }

    @Override
    public User updateUser(User user) {
        log.debug("updateUser, user update, call super");
        return super.update(user);
    }

    @Override
    public User deleteUserById(long id) {
        log.debug(format("deleteUserById, delete user by id=%d, call super", id));
        return super.deleteById(id);
    }

}
