package akvelon.zuora.denysenko.controller;

import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.exception.ChangesNotPersistedException;
import akvelon.zuora.denysenko.mapper.mapstruct.api.TaskApiMapper;
import akvelon.zuora.denysenko.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Task controller URL
 * <p>./planning-system/v1/tasks</p>
 *
 * @author Denysenko Stanislav
 * <a href="mailto:stanislav.denisenko@akvelon.com">mail</a>
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "planning-system/v1/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskApiMapper taskApiMapper;

    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_ORDER = "ASC";

    /**
     * Create Task.
     * <p>Post request:
     * {@link}http://localhost:8081/planning-system/v1/tasks
     * <p>
     * Json body
     * <p>
     * {"name": "First",
     * "description": "try",
     * "status": "READY",
     * "priority": "HIGH"}
     * <p>Validation:
     * <p>name length: min = 2, max = 100 chars
     * <p>description length max 500 chars
     * <p>
     * <p>Options for Status:</p>
     * <p>TO DO
     * <p>READY
     * <p>IN_PROGRESS
     * <p>COMPLETED
     * <p>
     * <p>Options for Priority:</p>
     * <p>LOW
     * <p>HIGH
     * <p>MEDIUM
     * <p>CRITICAL
     *
     * @param taskApi TaskApi mapped from json request
     * @return TaskApi saved into database
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TaskApi> createTask(@Valid @RequestBody TaskApi taskApi) {
        log.debug("createTask, Post request, taskApi json: {}", taskApi);
        TaskApi taskResponse = taskApiMapper.toTaskApi(taskService.createTask(taskApiMapper.toTask(taskApi)));
        log.debug("Task created: {}", taskApi);
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    /**
     * Update Task.
     * <p>Patch request:
     * {@link}http://localhost:8081/planning-system/v1/tasks
     * <p>
     * {"ID": 1,
     * "name": "new Name",
     * "description": "new Description",
     * "status": "READY",
     * "priority": "HIGH"}
     *
     * @param taskApi Task mapped from json request
     * @return updated TaskApi entity
     */
    @PatchMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi updateTask(@RequestBody TaskApi taskApi) throws ChangesNotPersistedException {
        log.debug("updateTask, patch request: {}", taskApi);
        TaskApi responseTaskApi = taskApiMapper.toTaskApi(taskService.updateTask(taskApiMapper.toTask(taskApi)));
        log.debug("updateTask, task updated, response: {}", responseTaskApi);
        return responseTaskApi;
    }

    /**
     * Delete Task by id.
     * <p>Delete request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0
     *
     * @param id Task id
     * @return deleted TaskApi entity
     */
    @DeleteMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi deleteTaskById(@PathVariable long id) {
        log.debug("Delete request, deleteTaskById: id= {}", id);
        TaskApi taskApiResponse = taskApiMapper.toTaskApi(taskService.deleteTaskById(id));
        log.debug("Deleted task by id, response: {}", taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Get TaskApi by id.
     * <p>Get request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0
     *
     * @param id Task id
     * @return TaskApi entity by requested id
     */
    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi getTaskById(@PathVariable long id) {
        log.debug("getTaskById, Get request: id={}", id);
        TaskApi taskApiResponse = taskApiMapper.toTaskApi(taskService.getTaskById(id));
        log.debug("getTaskById, Got task, response: {}", taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Get tasks.
     * <p>Get Request:
     * {@link}http://localhost:8081/planning-system/v1/tasks?sort=id&order=desc
     *
     * @param sort  Can sort by: Id, Date, Status, Priority
     * @param order default = ASC, DESC
     * @return List of all TaskApi entities
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TaskApi> getTasks(@RequestParam(required = false, defaultValue = DEFAULT_SORT) String sort,
                                  @RequestParam(required = false, defaultValue = DEFAULT_ORDER) String order) {
        log.debug("getTasks, Get request: sort={}, order={}", sort, order);
        List<TaskApi> taskApiResponse = taskService.getTasks(sort, order).stream()
                                                   .map(taskApiMapper::toTaskApi).collect(Collectors.toList());
        log.debug("Got tasks, response: {}", taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Assign user on task.
     * <p>Put request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0/assign/0
     *
     * @param userId user id whom to assign on task
     * @param taskId task id where to assign
     * @return updated TaskApi entity
     */
    @PutMapping(value = "{taskId}/assign/{userId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi assignUserOnTask(@PathVariable long taskId, @PathVariable long userId) {
        log.debug("assignUserOnTask, patch request, user={}, task={}: ", userId, taskId);
        TaskApi taskApiResponse = taskApiMapper.toTaskApi(taskService.assignUserOnTask(taskId, userId));
        log.debug("assignUserOnTask, user {} assigned on task id={}, response: {}", userId, taskId, taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Unassign user from task.
     * <p>Put request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0/unassign/0
     *
     * @param userId user id whom to be unassigned
     * @param taskId task id from what task
     * @return updated TaskApi entity
     */
    @PutMapping(value = "{taskId}/unassign/{userId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi unAssignUserFromTask(@PathVariable long taskId, @PathVariable long userId) {
        log.debug(format("unAssignUserFromTask, patch request, unAssignUser=%d, FromTask=%s: ", userId, taskId));
        TaskApi taskResponse = taskApiMapper.toTaskApi(taskService.unAssignUserFromTask(taskId, userId));
        log.debug("unAssignUserFromTask, user {} unassigned from task id={}, response: {}", userId, taskId, taskResponse);
        return taskResponse;
    }

    /**
     * Subscribe user on task.
     * <p>Put request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0/subscribe/0
     *
     * @param taskId task id to subscribe on
     * @param userId user id to be subscribed
     * @return updated TaskAPi entity
     */
    @PutMapping(value = "{taskId}/subscribe/{userId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi subscribeUserOnTask(@PathVariable long taskId, @PathVariable long userId) {
        log.debug("subscribeUserOnTask, put request, subscribe user={}, on task={}: ", userId, taskId);
        TaskApi taskApiResponse = taskApiMapper.toTaskApi(taskService.subscribeUserOnTask(taskId, userId));
        log.debug("subscribeUserOnTask, user subscribed on task, response: {}", taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Unsubscribe user from task.
     * <p>Put request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/0/unsubscribe/0
     *
     * @param taskId task id to be unsubscribed from
     * @param userId user id to be unsubscribed from task
     * @return updated TaskApi entity
     */
    @PutMapping(value = "{taskId}/unsubscribe/{userId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public TaskApi unSubscribeUserFromTask(@PathVariable long taskId, @PathVariable long userId) {
        log.debug("unSubscribeUserFromTask, put request, unsubscribe user={}, from task={}: ", userId, taskId);
        TaskApi taskApiResponse = taskApiMapper.toTaskApi(taskService.unSubscribeUserFromTask(taskId, userId));
        log.debug("unSubscribeUserFromTask, user unsubscribed from task, response: {}", taskApiResponse);
        return taskApiResponse;
    }

    /**
     * Get user subscriptions.
     * <p>Get request:
     * {@link}http://localhost:8081/planning-system/v1/tasks/users/0
     *
     * @param userId user id whose subscription to get
     * @param sort   can sort by: id, name, description, dateAdded
     * @param order  default = asc, desc
     * @return List TaskApi entities corresponding to the requested user
     */
    @GetMapping(value = "users/{userId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<TaskApi> getSubscribedTasksByUserId(@PathVariable long userId,
                                                    @RequestParam(required = false, defaultValue = DEFAULT_SORT) String sort,
                                                    @RequestParam(required = false, defaultValue = DEFAULT_ORDER) String order) {
        log.debug("getSubscribedTasksByUserId, user={}, sort={}, order={}", userId, sort, order);
        List<TaskApi> taskApiResponse = taskService.getSubscribedTasksByUserId(userId, sort, order).stream()
                                                   .map(taskApiMapper::toTaskApi).collect(Collectors.toList());
        log.debug("getSubscribedTasksByUserId, response: {}", taskApiResponse);
        return taskApiResponse;
    }

}
