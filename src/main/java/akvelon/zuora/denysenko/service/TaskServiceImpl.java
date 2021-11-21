package akvelon.zuora.denysenko.service;

import akvelon.zuora.denysenko.entity.*;
import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.exception.*;
import akvelon.zuora.denysenko.kafka.producer.task.TaskKafkaProducer;
import akvelon.zuora.denysenko.mapper.mapstruct.api.TaskApiMapper;
import akvelon.zuora.denysenko.mapper.mapstruct.api.UserApiMapper;
import akvelon.zuora.denysenko.mapper.mapstruct.dao.TaskFieldsMapper;
import akvelon.zuora.denysenko.persistence.TaskDAO;
import akvelon.zuora.denysenko.persistence.UserDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service layer for Task operations.
 *
 * @author Denysenko Stanislav
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskDAO taskDAO;
    private final UserDAO userDAO;

    private final TaskApiMapper taskApiMapper;
    private final TaskFieldsMapper taskFieldsMapper;
    private final UserApiMapper userApiMapper;

    private final TaskKafkaProducer taskKafkaProducer;

    /**
     * Create Task.
     * Default values if not specified:
     * <p>If status - TO DO
     * <p>If priority - LOW
     *
     * @param task Task to create
     * @return created Task
     * @throws BadRequestException          if attempting create Task with COMPLETED status
     * @throws ChangesNotPersistedException if Task was not created
     */
    public Task createTask(Task task) throws BadRequestException, ChangesNotPersistedException {
        log.debug("createTask, request task: {}", task);
        if (Objects.isNull(task.getStatus())) {
            task.setStatus(Status.TODO);
            log.debug("createTask, Set default status: " + task);
        }
        if (Objects.isNull(task.getPriority())) {
            task.setPriority(Priority.LOW);
            log.debug("createTask, Set default priority: " + task);
        }
        if (task.getStatus().equals(Status.COMPLETED)) {
            log.debug("createTask, Task can't be created with completed status, request json: " + task);
            throw new BadRequestException("Task can not be created with COMPLETED status, request: " + task);
        }

        Task taskResponse = taskDAO.createTask(task);
        if (Objects.isNull(taskResponse)) {
            log.debug("createTask, Task wasn't created, request json: " + task);
            throw new ChangesNotPersistedException("Task was not created, request: " + task);
        }
        log.debug("createTask, Task created, response: {}", taskResponse);
        taskKafkaProducer.sendCreatedTaskKafkaEvent(taskApiMapper.toTaskApi(taskResponse));
        return taskResponse;
    }

    /**
     * Update Task.
     * Can't update COMPLETED Task
     *
     * @param task Task Entity with changed fields
     * @return updated Task entity
     * @throws TaskNotFoundException        if Task task not found
     * @throws ChangesNotPersistedException if Task was not updated
     */
    public Task updateTask(Task task) throws TaskNotFoundException, ChangesNotPersistedException {
        long taskId = task.getId();
        Task taskDb = taskDAO.getTaskById(taskId);
        if (Objects.isNull(taskDb)) {
            log.debug("updateTask, task wasn't found, request: {}", task);
            throw new TaskNotFoundException("Enter valid task id for update: id= " + taskId);
        }
        if (task.equals(taskDb)) {
            log.debug("updateTask, task for update isn't changed");
            return task;
        }
        if (taskDb.getStatus().equals(Status.COMPLETED)) {
            log.debug("updateTask, task status can't be changed because it's completed");
            return taskDb;
        }

        Task taskBeforeUpdate = taskDb.toBuilder().build();

        Task taskUpdated = taskFieldsMapper.mapRequestTaskToTask(task, taskDb);
        Task response = taskDAO.updateTask(taskUpdated);
        if (Objects.isNull(response)) {
            log.debug("updateTask, task wasn't updated, request: {}", taskUpdated);
            throw new ChangesNotPersistedException("updateTask, task wasn't updated, id= " + taskUpdated.getId());
        }
        log.debug("updateTask, task updated, response: {}", response);
        taskKafkaProducer.sendUpdatedTaskKafkaEvent(
                taskApiMapper.toTaskApi(taskBeforeUpdate), taskApiMapper.toTaskApi(response));
        return response;
    }

    /**
     * Delete Task by id.
     *
     * @param id requested id of the task
     * @return Task Entity of this ID
     * @throws ChangesNotPersistedException if Task was not deleted
     */
    public Task deleteTaskById(long id) throws ChangesNotPersistedException {
        log.debug("deleteTaskById, request: {}", id);
        Task task = deleteTaskByIdPrivate(id);
        taskKafkaProducer.sendDeletedTaskKafkaEvent(taskApiMapper.toTaskApi(task));
        return task;
    }

    public Task rejectTaskById(long id) throws ChangesNotPersistedException {
        log.debug("rejectTaskById, request: {}", id);
        Task task = deleteTaskByIdPrivate(id);
        taskKafkaProducer.sendDeletedTaskKafkaEvent(taskApiMapper.toTaskApi(task));
        return task;
    }

    private Task deleteTaskByIdPrivate(long id) throws ChangesNotPersistedException {
        log.debug("deleteTaskById, request: {}", id);
        Task task = taskDAO.deleteTaskById(id);
        if (Objects.isNull(task)) {
            log.warn("deleteTaskById, task can not be deleted: {}", id);
            throw new ChangesNotPersistedException("Task wasn't deleted, id = " + id);
        }
        log.debug("deleteTaskById, deleted task: {}", task);
        return task;
    }

    /**
     * Get tasks.
     *
     * @param sort  can sort by: id, name, description, dateAdded, status , priority
     * @param order default = ASC, DESC
     * @return List of all tasks
     */
    public List<Task> getTasks(String sort, String order) {
        log.debug("getTasks: sort={}, order={}", sort, order);
        List<Task> taskResponse = taskDAO.getTasks(sort, order);
        log.debug("Got tasks, response: {}", taskResponse);
        return taskResponse;
    }

    /**
     * @param id requested id of the task
     * @return Task Entity of this ID
     */
    public Task getTaskById(long id) throws TaskNotFoundException {
        log.debug("getTaskById: id={}", id);

        Task taskResponse = taskDAO.getTaskById(id);
        if (Objects.isNull(taskResponse)) {
            log.info("Task not found: id={}", id);
            throw new TaskNotFoundException("Task not found: id = " + id);
        }
        log.debug("Got task by id, response: {}", taskResponse);
        return taskResponse;
    }

    /**
     * Assign User on Task.
     * User Can't be assigned on a finished task.
     *
     * @param userId User id to assign
     * @param taskId Task id assign to
     * @return updated Task
     * @throws TaskNotFoundException if Task assign to to not found
     * @throws UserNotFoundException if User to assign not found
     */
    @Override
    public Task assignUserOnTask(long taskId, long userId) throws TaskNotFoundException, UserNotFoundException {
        Task taskDb = taskDAO.getTaskById(taskId);
        if (Objects.isNull(taskDb)) {
            log.debug("Task to assign is null, request: user id = {}, task id = {}", userId, taskId);
            throw new TaskNotFoundException("Task to assign isn't found, id= " + taskId);
        }
        User userDb = userDAO.getUserById(userId);
        if (Objects.isNull(userDb)) {
            log.debug("User to assign is null, request: user id = {}, task id = {}", userId, taskId);
            throw new UserNotFoundException("User to assign isn't found, id= " + taskId);
        }
        if (!Objects.isNull(taskDb.getUser()) && userId == taskDb.getUser().getId()) {
            log.debug("Trying to assign user that already assigned on the task ");
            return taskDb;
        }
        if (taskDb.getSubscribers().contains(userDb)) {
            log.debug("assignUserOnTask, trying to assign on task that you subscribed on, please unsubscribe first");
            throw new ChangesNotPersistedException("You are trying to assign on task, that you subscribed to, please unsubscribe first");
        }
        if (taskDb.getStatus().equals(Status.COMPLETED)) {
            log.debug("Trying to assign task that was completed");
            return taskDb;
        }

        Task taskBeforeAssign = taskDb.toBuilder().build();

        taskDb.setUser(userDb);
        Task updatedTask = taskDAO.updateTask(taskDb);
        if (Objects.isNull(updatedTask)) {
            log.debug("Unable to assign User: id={} on task: id={}", userId, taskId);
            throw new ChangesNotPersistedException("User was not assigned on task, request: " + taskDb);
        }
        taskKafkaProducer.sendUserAssignedKafkaEvent(taskApiMapper.toTaskApi(taskBeforeAssign),
                taskApiMapper.toTaskApi(updatedTask), userApiMapper.toUserApi(userDb));
        log.debug("User assigned on task: id={}", updatedTask);
        return updatedTask;
    }

    /**
     * Unassign User fromTask.
     * Only if been assigned.
     *
     * @param taskId Task id to unassign from
     * @param userId User id
     * @return Updated Task
     * @throws TaskNotFoundException if Task to unnassign from not found
     * @throws UserNotFoundException if User to unassign not found
     */
    @Override
    public Task unAssignUserFromTask(long taskId, long userId) throws TaskNotFoundException, UserNotFoundException {
        Task taskDb = taskDAO.getTaskById(taskId);
        if (Objects.isNull(taskDb)) {
            log.debug("unAssignUserFromTask, unable to unassign user: id={} from task: id={}", userId, taskId);
            throw new TaskNotFoundException("Task not found, id= " + taskId);
        }

        if (!Objects.isNull(taskDb.getUser()) && userId != taskDb.getUser().getId()) {
            log.debug("unAssignUserFromTask, trying to unassign user that was not assigned on the task");
            return taskDb;
        }

//        User unassignedUser = taskDb.getUser().toBuilder().build();
        Task beforeUnAssign = taskDb.toBuilder().build();

        taskDb.setUser(null);
        Task responseTask = taskDAO.updateTask(taskDb);
        if (Objects.isNull(responseTask)) {
            log.debug("unAssignUserFromTask, unable to unassign user from task, user: id={}, task: id={}", userId, taskId);
            throw new ChangesNotPersistedException("User was not unassigned from task, request: " + taskDb);
        }
        log.debug("unAssignUserFromTask, user was unassigned from task, user: id={}, task: id={}", userId, taskId);
        taskKafkaProducer.sendUserUnassignedKafkaEvent(taskApiMapper.toTaskApi(beforeUnAssign),
                taskApiMapper.toTaskApi(responseTask), userApiMapper.toUserApi(beforeUnAssign.getUser()));
        return responseTask;
    }


    /**
     * Subscribe User on Task.
     * Can't subscribe assigned User.
     *
     * @param taskId Task id to subscribe on
     * @param userId User id to subscribe
     * @return updated Task
     * @throws TaskNotFoundException        if Task to subscribe on not found
     * @throws UserNotFoundException        if User to subscribe not found
     * @throws ChangesNotPersistedException if User trying to subscribe on assigned Task,
     *                                      or User was not subscribe and task wasn't updated
     */
    @Override
    public Task subscribeUserOnTask(long taskId, long userId)
            throws TaskNotFoundException, UserNotFoundException, ChangesNotPersistedException {
        Task taskDb = taskDAO.getTaskById(taskId);
        if (Objects.isNull(taskDb)) {
            log.debug("subscribeUserOnTask, task to subscribe is null: taskId={}, userId={}", taskId, userId);
            throw new TaskNotFoundException("Task to subscribe isn't found, id= " + taskId);
        }
        User userDb = userDAO.getUserById(userId);
        if (Objects.isNull(userDb)) {
            log.debug("subscribeUserOnTask, user to subscribe is null, userId={}", userId);
            throw new UserNotFoundException("User to subscribe isn't found, id= " + userId);
        }
        if (userDb.equals(taskDb.getUser())) { // user to subscribe, user who assigned
            log.debug("subscribeUserOnTask, user assigned on task trying to subscribe on the task");
            throw new ChangesNotPersistedException("You are trying to subscribe on task, that you assigned to do");
        }

        Set<User> subscribersBefore = taskDb.getSubscribers();
        if (!Objects.isNull(subscribersBefore)) {
            // copy set of subscribers in not updated condition
            subscribersBefore = new HashSet<>(subscribersBefore);
        }
        Task beforeSubscribe = taskDb.toBuilder().subscribers(subscribersBefore).build();

        Set<User> subscribers = taskDb.getSubscribers();
        if (Objects.isNull(subscribers)) {
            subscribers = new HashSet<>();
            taskDb.setSubscribers(subscribers);
        }
        if (subscribers.contains(userDb)) {
            log.debug("subscribeUserOnTask, trying to subscribe user that already subscribed on the task: {}", taskDb);
            return taskDb;
        }
        subscribers = new HashSet<>(taskDb.getSubscribers());
        subscribers.add(userDb);
        taskDb.setSubscribers(subscribers);

        Task updatedTask = taskDAO.updateTask(taskDb);
        log.debug("subscribeUserOnTask -> updated task, response: {}", updatedTask);
        if (Objects.isNull(updatedTask)) {
            log.debug("subscribeUserOnTask, unable to subscribe User: id={} on task: id={}", userId, taskId);
            throw new ChangesNotPersistedException("User was not subscribed on task, request: " + taskDb);
        }
        log.debug("subscribeUserOnTask, user subscribed on task, response: {}", updatedTask);
        taskKafkaProducer.sendUserSubscribedKafkaEvent(taskApiMapper.toTaskApi(beforeSubscribe),
                taskApiMapper.toTaskApi(updatedTask), userApiMapper.toUserApi(userDb));
        return updatedTask;
    }

    /**
     * Unsubscribe User from Task.
     *
     * @param taskId Task id to unsubscribe from
     * @param userId User id to unsubscribe
     * @return updated Task
     * @throws TaskNotFoundException        if Task to unsubscribe from not found
     * @throws UserNotFoundException        if User to unsubscribe not found
     * @throws ChangesNotPersistedException if User was not unsubscribed from
     */
    @Override
    public Task unSubscribeUserFromTask(long taskId, long userId)
            throws TaskNotFoundException, UserNotFoundException, ChangesNotPersistedException {
        Task taskDb = taskDAO.getTaskById(taskId);
        if (Objects.isNull(taskDb)) {
            log.debug("unSubscribeUserFromTask, task to unsubscribe is null: taskId={}, userId={}", taskId, userId);
            throw new TaskNotFoundException("subscribeUserOnTask, task to unsubscribe isn't found, id= " + taskId);
        }
        User userDb = userDAO.getUserById(userId);
        if (Objects.isNull(userDb)) {
            log.debug("unSubscribeUserFromTask, user to unsubscribe is null, userId={}", userId);
            throw new UserNotFoundException("User to unsubscribe isn't found, id= " + taskId);
        }

        Set<User> subscribersBefore = taskDb.getSubscribers();
        if (!Objects.isNull(subscribersBefore)) {
            // copy set of subscribers in not updated condition
            subscribersBefore = new HashSet<>(subscribersBefore);
        }
        Task beforeSubscribe = taskDb.toBuilder().subscribers(subscribersBefore).build();

        Set<User> subscribers = taskDb.getSubscribers();
        if (Objects.isNull(subscribers)) {
            log.debug("unSubscribeUserFromTask, trying to unsubscribe not subscribed user, subs is empty: {}", userDb);
            return taskDb;
        }
        if (!subscribers.contains(userDb)) {
            log.debug("unSubscribeUserFromTask, trying to unsubscribe not subscribed user, susbs does not contain: {}", userDb);
            return taskDb;
        }

        subscribers.removeIf(userDb::equals);
        if (subscribers.size() == 0) {
            taskDb.setSubscribers(null);
        }

        Task updatedTask = taskDAO.updateTask(taskDb);
        if (Objects.isNull(updatedTask)) {
            log.debug("unSubscribeUserFromTask, unable to unsubscribe User: id={} from task: id={}", userId, taskId);
            throw new ChangesNotPersistedException("User was not unsubscribed from task, request: " + taskDb);
        }
        log.debug("unSubscribeUserFromTaskUnable, user unsubscribed from task, response: {}", updatedTask);
        taskKafkaProducer.sendUserSubscribedKafkaEvent(taskApiMapper.toTaskApi(beforeSubscribe),
                taskApiMapper.toTaskApi(updatedTask), userApiMapper.toUserApi(userDb));
        return updatedTask;
    }

    /**
     * Get subscribed tasks by Uer id.
     *
     * @param userId User id
     * @param sort   can sort by: id,
     * @param order  default = ASC, DESC
     * @return List tasks by user id
     */
    @Override
    public List<Task> getSubscribedTasksByUserId(long userId, String sort, String order) {
        log.debug("getSubscribedTasksByUserId: userId={}, sort={}, order={}}", userId, sort, order);
        List<Task> response = taskDAO.getSubscribedTasksByUserId(userId, sort, order);
        log.debug("getSubscribedTasksByUserId, got tasks, response: {}", response);
        return response;
    }

}
