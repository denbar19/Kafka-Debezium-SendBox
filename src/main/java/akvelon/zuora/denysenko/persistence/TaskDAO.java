package akvelon.zuora.denysenko.persistence;

import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;

import java.util.List;

/**
 * @author Denysenko Stanislav
 */
public interface TaskDAO  {

    /**
     * Saves the Task.
     *
     * @param task to be created
     * @return created Task
     */
    Task createTask(Task task);

    /**
     * Overwrites changed fields inf corresponding task.
     *
     * @param task Task entity  with desired fields
     * @return updated Task entity
     */
    Task updateTask(Task task);

    /**
     * Return's one task with requested id.
     *
     * @param id Task id
     * @return Task entity
     */
    Task getTaskById(long id);

    /**
     * Delete one task with requested id.
     *
     * @param id Task id
     * @return 1 if successful, -1 if no
     */
    Task deleteTaskById(long id);

    /**
     * Get Tasks by corresponding filter.
     *
     * @param sort  field name to sort by
     * @param order default - ASC, DESC
     * @return List of Task entities
     */
    List<Task> getTasks(String sort, String order);

    /**
     * Delete All Tasks that have status = 0 ({@code TO DO}), and date of creation less then now minus {@code timePeriod}.
     *
     * @param timePeriod time is seconds that will be taken away from the present moment
     * @return List of tasks to reject
     */
    List<Task> getRejectionTasks(long timePeriod);

    Task setUserToNullForTask(Task task);

    Task unAssignUserFromTask(Task task);

    User unAssignUserFromHisTasks(User user);

    List<Task> getSubscribedTasksByUserId(long userId, String sort, String order);
}
