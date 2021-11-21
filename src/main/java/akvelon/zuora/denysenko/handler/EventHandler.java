package akvelon.zuora.denysenko.handler;

import akvelon.zuora.denysenko.entity.EntityAction;
import akvelon.zuora.denysenko.entity.Status;
import akvelon.zuora.denysenko.entity.TaskUpdatedAction;
import akvelon.zuora.denysenko.entity.api.TaskApi;
import akvelon.zuora.denysenko.entity.api.UserApi;
import akvelon.zuora.denysenko.exception.TaskNotFoundException;
import akvelon.zuora.denysenko.exception.UserNotFoundException;
import akvelon.zuora.denysenko.exception.notification.email.NotificationEmailException;
import akvelon.zuora.denysenko.kafka.event.AbstractKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskCreatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskDeletedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskRejectedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.task.TaskUpdatedKafkaEvent;
import akvelon.zuora.denysenko.kafka.event.user.*;
import akvelon.zuora.denysenko.validation.AbstractValidator;
import akvelon.zuora.denysenko.validation.task.TaskApiValidator;
import akvelon.zuora.denysenko.validation.user.UserApiValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static akvelon.zuora.denysenko.entity.EntityAction.*;
import static akvelon.zuora.denysenko.entity.TaskUpdatedAction.*;

/**
 * Each Kafka event must be processed in some way.
 *
 * @author Denysenko Stanislav
 */
@Slf4j
@RequiredArgsConstructor
@Component("EventHandler")
public class EventHandler {

    public final TaskApiValidator<TaskApi> taskApiValidator;
    public final UserApiValidator<UserApi> userApiValidator;
    public final AbstractValidator<UserApi> validator;

    private final Map<EntityAction, Consumer<AbstractKafkaEvent>> eventsOptions = new HashMap<>() {{
        put(TASK_CREATED, (e) -> processTaskCreated((TaskCreatedKafkaEvent) e));
        put(EntityAction.TASK_UPDATED, (e) -> processTaskUpdatedEvents((TaskUpdatedKafkaEvent) e));
        put(TASK_DELETED, (e) -> processTaskDeleted((TaskDeletedKafkaEvent) e));
        put(TASK_REJECTED, (e) -> processTaskRejected((TaskRejectedKafkaEvent) e));
        put(USER_CREATED, (e) -> processUserCreated((UserCreatedKafkaEvent) e));
        put(USER_UPDATED, (e) -> processUserUpdated((UserUpdatedKafkaEvent) e));
        put(USER_DELETED, (e) -> processUserDeleted((UserDeletedKafkaEvent) e));

    }};

    private final Map<TaskUpdatedAction, Consumer<AbstractKafkaEvent>> taskUpdateEventsOptions = new HashMap<>() {{
        put(TaskUpdatedAction.TASK_UPDATED, (e) -> processTaskUpdated((TaskUpdatedKafkaEvent) e));

        put(USER_ASSIGNED, (e) -> processUserAssigned((UserAssignedKafkaEvent) e));
        put(USER_UNASSIGNED, (e) -> processUserUnassigned((UserUnassignedKafkaEvent) e));
        put(USER_SUBSCRIBED, (e) -> processUserSubscribed((UserSubscribedKafkaEvent) e));
        put(USER_UNSUBSCRIBED, (e) -> processUserUnsubscribed((UserUnsubscribedKafkaEvent) e));

    }};

    /**
     * Process every event from Kafka Consumer and redirect on to correct processing method.
     *
     * @param abstractKafkaEvent AbstractKafkaEvent
     */
    public void processEvent(AbstractKafkaEvent abstractKafkaEvent) {
        log.debug("processEvent, {}", abstractKafkaEvent);
        if (Objects.isNull(abstractKafkaEvent)) {
            log.debug("processEvent, abstractKafkaEvent is null");
            return;
        }
        eventsOptions.get(abstractKafkaEvent.getAction()).accept(abstractKafkaEvent);
    }

    /**
     * Process events that connected to Task update.
     *
     * @param taskUpdatedKafkaEvent
     */
    private void processTaskUpdatedEvents(TaskUpdatedKafkaEvent taskUpdatedKafkaEvent) {
        log.debug("processTaskUpdateEvents, {}", taskUpdatedKafkaEvent);
        taskUpdateEventsOptions.get(taskUpdatedKafkaEvent.getTaskUpdatedAction()).accept(taskUpdatedKafkaEvent);
    }

    /**
     * Nothing is happening on this event.
     *
     * @param task TaskCreatedKafkaEvent
     */
    private void processTaskCreated(TaskCreatedKafkaEvent task) {
        log.debug("processTaskCreate {}", task);
    }

    /**
     * Find and manage statistic by incoming task changes.
     * <p>
     * Finds differences it task update, and save corresponding changes id statistic
     * <p>
     * Send Email notifications on update.
     *
     * @param event TaskUpdatedKafkaEvent
     */
    private void processTaskUpdated(TaskUpdatedKafkaEvent event) {
        log.debug("processTaskUpdate, {}", event);

        TaskApi beforeUpdateTaskApi = taskApiValidator.isNull(event.getBeforeUpdateTask());
        TaskApi updatedTaskApi = taskApiValidator.isNull(event.getAfterUpdateTask());

        if (beforeUpdateTaskApi.equals(updatedTaskApi)) {
            log.info("processTaskUpdate, task from Updated event not changed");
            return;
        }

        // Check Status COMPLETED
        if (updatedTaskApi.getStatus().equals(Status.COMPLETED)) {
            log.debug("processTaskUpdate, manage task status changes");

            if (!Objects.isNull(updatedTaskApi.getSubscribersApi()) && updatedTaskApi.getSubscribersApi().size() != 0) {
                try {


                } catch (NotificationEmailException exception) {
                    log.warn(exception.getMessage());
                }
            }
            return;
        }

        if (!Objects.isNull(updatedTaskApi.getSubscribersApi()) && updatedTaskApi.getSubscribersApi().size() != 0) {
            try {


            } catch (NotificationEmailException exception) {
                log.warn(exception.getMessage());
            }
        }
    }


    /**
     * Send Email notifications on delete.
     *
     * @param event
     */
    private void processTaskDeleted(TaskDeletedKafkaEvent event) {
        log.debug("processTaskDelete, {}", event);
        TaskApi taskApi = event.getTask();

        if (!Objects.isNull(taskApi.getSubscribersApi()) && taskApi.getSubscribersApi().size() != 0) {
            try {


            } catch (NotificationEmailException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * Updates statistic of user, which was assigned on the {@code task}.
     *
     * @param event rejected TaskApi
     */
    private void processTaskRejected(TaskRejectedKafkaEvent event) {
        log.debug("processTaskReject, {}", event);
        TaskApi taskApi = event.getTask();
        UserApi userApi = taskApi.getUserApi();
        if (Objects.isNull(userApi)) {
            log.debug("processTaskReject, user is null, request: {}", event);
            throw new UserNotFoundException("User not found, request: " + event);
        }


        if (!Objects.isNull(taskApi.getSubscribersApi()) && taskApi.getSubscribersApi().size() != 0) {
            try {


            } catch (NotificationEmailException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    /**
     * When user created, statistic record also should be created.
     *
     * @param event UserCreatedKafkaEvent
     */
    private void processUserCreated(UserCreatedKafkaEvent event) {
        log.debug("processUserCreate, {}", event);
    }

    /**
     * Personal information update when user is updated.
     *
     * @param event UserUpdatedKafkaEvent
     */
    private void processUserUpdated(UserUpdatedKafkaEvent event) {
        log.debug("processUserUpdate, {}", event);
    }

    /**
     * Checking, is statistic alive after user been deleted.
     *
     * @param event UserDeletedKafkaEvent
     */
    private void processUserDeleted(UserDeletedKafkaEvent event) {
        log.debug("processUserDelete, {}", event);
    }

    private void processUserAssigned(UserAssignedKafkaEvent event) {
        log.debug("processUserAssign, {}", event);

        TaskApi beforeUpdateTaskApi = event.getBeforeUpdateTask();
        if (Objects.isNull(beforeUpdateTaskApi)) {
            log.warn("processTaskUpdate, task before update is null, request: {}", event);
            throw new TaskNotFoundException("Task before update is null, request: " + event);
        }

        TaskApi updatedTaskApi = event.getAfterUpdateTask();
        if (Objects.isNull(updatedTaskApi)) {
            log.warn("processUserAssigned, task after update is null, request: {}", event);
            throw new TaskNotFoundException("Task after update is null, request: " + event);
        }

        UserApi userApi = updatedTaskApi.getUserApi();
        if (Objects.isNull(userApi)) {
            log.warn("processUserAssigned, task after update is null, request: {}", event);
            throw new UserNotFoundException("Assinged user is null, request: " + event);
        }


        if (!Objects.isNull(updatedTaskApi.getSubscribersApi()) && updatedTaskApi.getSubscribersApi().size() != 0) {
            try {


            } catch (NotificationEmailException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    private void processUserUnassigned(UserUnassignedKafkaEvent event) {
        log.debug("processUserUnassign, {}", event);

        TaskApi beforeUpdateTaskApi = event.getBeforeUpdateTask();
        if (Objects.isNull(beforeUpdateTaskApi)) {
            log.warn("processUserUnassign, task before update is null, request: {}", event);
            throw new TaskNotFoundException("Task before update is null, request: " + event);
        }

        TaskApi updatedTaskApi = event.getAfterUpdateTask();
        if (Objects.isNull(updatedTaskApi)) {
            log.warn("processUserUnassign, task after update is null, request: {}", event);
            throw new TaskNotFoundException("Task after update is null, request: " + event);
        }

        UserApi userApi = updatedTaskApi.getUserApi();
        if (Objects.isNull(userApi)) {
            log.warn("processUserUnassign, user is null, request: {}", event);
            throw new UserNotFoundException("Assinged user is null, request: " + event);
        }

        if (!Objects.isNull(updatedTaskApi.getSubscribersApi()) && updatedTaskApi.getSubscribersApi().size() != 0) {
            try {


            } catch (NotificationEmailException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    private void processUserSubscribed(UserSubscribedKafkaEvent event) {
        log.debug("processUserSubscribed, {}", event);
    }

    private void processUserUnsubscribed(UserUnsubscribedKafkaEvent event) {
        log.debug("processUserUnsubscribed, {}", event);
    }


}
