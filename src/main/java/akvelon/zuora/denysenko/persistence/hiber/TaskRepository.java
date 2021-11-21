package akvelon.zuora.denysenko.persistence.hiber;

import akvelon.zuora.denysenko.entity.persistence.Task;
import akvelon.zuora.denysenko.entity.persistence.User;
import akvelon.zuora.denysenko.exception.ChangesNotPersistedException;
import akvelon.zuora.denysenko.persistence.TaskDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.format;

/**
 * @author Denysenko Stanislav
 */
@Slf4j
@Repository("TaskRepository")
public class TaskRepository extends RepositoryImpl<Task> implements TaskDAO {

    @Autowired
    public TaskRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory, Task.class);
    }

    @Override
    public Task createTask(Task task) {
        log.debug("createTask, call super");
        return super.create(task);
    }

    @Override
    public Task updateTask(Task task) {
        log.debug("updateTask, task update, call super");
        return super.update(task);
    }

    @Override
    public Task getTaskById(long id) {
        log.debug("getTaskById, call super: id={}", id);
        return super.getById(id);
    }

    @Override
    public Task deleteTaskById(long id) {
        log.debug("deleteTaskById, call super: id={}", id);
        return super.deleteById(id);
    }

    @Override
    public List<Task> getTasks(String sort, String order) {
        log.debug("getTasks, call super: sort={}, order={}", sort, order);
        return super.getList(sort, order);
    }

    /**
     * Converts time from second to LocalDateTime for inserting in db query.
     *
     * @param timePeriod time in second, due to what date
     * @return converted time to LocalDateTime
     */
    private LocalDateTime convertTime(long timePeriod) {
        long rejectionLong = Instant.now().getEpochSecond() - timePeriod;
        log.debug("convertTime, rejections seconds: {}", rejectionLong);
        LocalDateTime rejectionLocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(rejectionLong), TimeZone.getDefault().toZoneId());
        log.debug(" convertTime, rejectionLocalDateTime: {}", rejectionLocalDateTime.toString());
        return rejectionLocalDateTime;
    }

    /**
     * Collect tasks that have Status "to do"(0), and date of creation before {@code timePeriod} seconds
     *
     * @param timePeriod time is seconds that will be taken away from the present moment
     * @return List Task to be rejected
     */
    public List<Task> getRejectionTasks(long timePeriod) {
        var entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<Task> query = entityManager.createQuery(
                "SELECT t FROM Task t WHERE t.status = 0 AND t.dateAdded <:rejectionLocalDateTime", Task.class);

        LocalDateTime rejectionLocalDateTime = this.convertTime(timePeriod);
        query.setParameter("rejectionLocalDateTime", rejectionLocalDateTime);

        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Task> tasks = query.getResultList();
            transaction.commit();
            if (!Objects.isNull(tasks)) {
                log.info("getRejectionTasks, tasks to be rejected: " + tasks);
                return tasks;
            }
        } catch (PersistenceException | IllegalStateException exception) {
            log.debug(format("getRejectionTasks, can't get tasks to reject, with dateAdded < %s, %s", rejectionLocalDateTime, exception.getMessage()));
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        return Collections.emptyList();
    }

    public Task setUserToNullForTask(Task task) {
        log.debug(format("unAssignUserFromHisTasks -> setUserToNullForTask: %s ", task));
        task.setUser(null);
        return this.updateTask(task);
    }

    @Override
    public User unAssignUserFromHisTasks(User user) {
        List<Task> userTasksList = user.getTasks();
        log.debug(format("unAssignUserFromHisTasks, unassign user: %d from his Tasks: %s ", user.getId(), userTasksList));
        List<Task> list = new LinkedList<>();
        for (Task task : userTasksList) {
            if (Objects.isNull(this.setUserToNullForTask(task))) {
                log.debug("Can't unassign user={} from task={}", user.getId(), task.getId());
                continue;
            }
            list.add(task);
        }
        log.debug("User={} was unassigned from his tasks: {}", user.getId(), list);
        return user;
    }

    @Override
    public Task unAssignUserFromTask(Task task) {
        task.setUser(null);
        Task response = this.updateTask(task);
        if (Objects.isNull(response)) {
            log.warn("User was not unassigned from task, request: {}", task);
            throw new ChangesNotPersistedException("User was not unassigned from task, request: " + task);
        }
        log.debug("deleteUserById, comment was detached from user");
        return response;
    }

    @Override
    public List<Task> getSubscribedTasksByUserId(long userId, String sort, String order) {
        var entityManager = entityManagerFactory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            List<Task> tasks = entityManager.createNamedQuery(Task.TasksByUserId, Task.class)
                    .setParameter("user_id", userId).getResultList();
            transaction.commit();
            if (!Objects.isNull(tasks)) {
                log.debug("getSubscribedTasksByUserId, response: {} ", tasks);
                return tasks;
            }
        } catch (PersistenceException | IllegalStateException exception) {
            log.debug("getSubscribedTasksByUserId, can't get comments by task: {} {}", userId, exception.getMessage());
            transaction.rollback();
        } finally {
            entityManager.close();
        }
        log.debug("getSubscribedTasksByUserId, null");
        return Collections.emptyList();
    }
}
