package akvelon.zuora.denysenko.controller;

import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.mapper.mapstruct.api.UserApiMapper;
import akvelon.zuora.denysenko.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * User controller:
 * ./planning-system/v1/users
 *
 * @author Denysenko Stanislav
 * <a href="mailto:stanislav.denisenko@akvelon.com">mail</a>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "planning-system/v1/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserApiMapper userApiMapper;
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_ORDER = "ASC";

    /**
     * Create User.
     * <p>Post request:
     * {@link}http://localhost:8081/planning-system/v1/users
     * <p>
     * Json body
     * <p>
     * {"name": "User",
     * "surname": "UserSurname",
     * "email": "mail@akvelon.com",
     * "role": "USER"}
     * <p>Validation:
     * <p>name length: min = 2,  max = 100 chars
     * <p>surname length: min = 2,  max = 100 chars
     * <p>email - valid email
     * <p>Options for Role:
     * <p>ADMIN
     * <p>USER
     * <p>SUPERVISOR
     * <p>DEVELOPER
     *
     * @param userApi UserApi mapped from json request
     * @return UserApi entity saved to database
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserApi createUser(@Valid @RequestBody UserApi userApi) {
        log.debug("Post request, json: {}", userApi);
        UserApi userResponse = userApiMapper.toUserApi(userService.createUser(userApiMapper.toUser(userApi)));
        log.debug("User created: {}", userResponse);
        return userResponse;
    }

    /**
     * Update user.
     * <p>Patch request:
     * {@link}http://localhost:8081/planning-system/v1/users
     * <p>
     * Json body
     * <p>
     * {"name": "User",
     * "surname": "UserSurname",
     * "email": "mail@akvelon.com"}
     *
     * @param userApi User mapped from json request
     * @return updated UserApi entity
     */
    @PatchMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserApi updateUser(@RequestBody UserApi userApi) {
        log.debug("Patch request, json: {}", userApi);
        UserApi userResponse = userApiMapper.toUserApi(userService.updateUser(userApiMapper.toUser(userApi)));
        log.debug("Updated user by id, response: {}", userResponse);
        return userResponse;
    }

    /**
     * Delete user by id.
     * <p>Delete request:
     * {@link}http://localhost:8081/planning-system/v1/users/0
     *
     * @param id User id
     * @return deleted UserApi entity
     */
    @DeleteMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserApi deleteUserById(@PathVariable long id) {
        log.debug("Delete request, deleteUserById: {}", id);
        UserApi userResponse = userApiMapper.toUserApi(userService.deleteUserById(id));
        log.debug("Deleted user by id, response: {}", userResponse);
        return userResponse;
    }

    /**
     * Get User by id.
     * <p>Get request:
     * {@link}http://localhost:8081/planning-system/v1/users/0
     *
     * @param id User id
     * @return UserApi entity
     */
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserApi getUserById(@PathVariable Long id) {
        log.debug("Get request, getUser: id={}", id);
        UserApi userResponse = userApiMapper.toUserApi(userService.getUserById(id));
        log.debug("Got user by id, response: {}", userResponse);
        return userResponse;
    }

    /**
     * Get users.
     * <p>Get request:
     * {@link}http://localhost:8081/planning-system/v1/users?sort=id&order=desc
     *
     * @param sort  Can sort by: Id, Role
     * @param order default = ASC, DESC
     * @return List of all UserApi entities by requested parameters
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<UserApi> getUsers(@RequestParam(required = false, defaultValue = DEFAULT_SORT) String sort,
                                  @RequestParam(required = false, defaultValue = DEFAULT_ORDER) String order) {
        log.debug("getUsers, get request, getUsers: sort={}, order={}", sort, order);
        List<UserApi> userResponse = userService.getUsers(sort, order).stream().
                map(userApiMapper::toUserApi).collect(Collectors.toList());
        log.debug("getUsers, response: {}", userResponse);
        return userResponse;
    }
}
