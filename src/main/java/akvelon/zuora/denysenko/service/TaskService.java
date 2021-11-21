package akvelon.zuora.denysenko.service;

import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.exception.*;

import java.util.List;

/**
 * CRUD operations on the Tasks
 *
 * @author Denysenko Stanislav
 */
public interface TaskService {

    /**
     * Creates new Task.
     *
     * @param task Task Entity with desired fields
     * @return created Task Entity
     */
    Task createTask(Task task);

    /**
     * Updates task content.
     *
     * @param task Task Entity with changed fields
     * @return updated Task entity
     */
    Task updateTask(Task task) throws TaskNotFoundException, ChangesNotPersistedException;

    /**
     * Delete Task with given id.
     *
     * @param id requested id of the task
     * @return Task Entity of this id
     */
    Task deleteTaskById(long id) throws TaskNotFoundException, UserNotFoundException, UnableToDeleteTask;

    Task rejectTaskById(long id) throws TaskNotFoundException, UserNotFoundException, UnableToDeleteTask;

    /**
     * Get Tasks List.
     *
     * @param sort     Name of sorting filed: ID, dateadded, Status , Priority
     * @param orderDir default = ASC, DESC
     * @return List of all tasks
     */
    List<Task> getTasks(String sort, String orderDir);

    /**
     * Get Task with given id.
     *
     * @param id task id
     * @return Task Entity of this id
     */
    Task getTaskById(long id) throws TaskNotFoundException;

    Task assignUserOnTask(long taskId, long userId) throws TaskNotFoundException, UserNotFoundException;

    Task unAssignUserFromTask(long taskId, long userId) throws TaskNotFoundException, UserNotFoundException;

    Task subscribeUserOnTask(long taskId, long userId);

    Task unSubscribeUserFromTask(long taskId, long userId);

    List<Task> getSubscribedTasksByUserId(long userId, String sort, String order);
}
