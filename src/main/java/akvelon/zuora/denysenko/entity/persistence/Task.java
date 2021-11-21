package akvelon.zuora.denysenko.entity.persistence;

import akvelon.zuora.denysenko.entity.AbstractTask;
import akvelon.zuora.denysenko.entity.Priority;
import akvelon.zuora.denysenko.entity.Status;
import lombok.*;
import org.hibernate.annotations.QueryHints;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Task entity. Contain: assigned {@code user} to do this task, {@code tags} to group tasks by some mark,
 * {@code comments} usual comments about task activities, {@code subscribers} users that are interested in task activity.
 * <p>dateAdded is set in {@code @PrePersist} method
 *
 * @author Denysenko Stanislav
 */
@Entity
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
// if i need to get "deleted" entities via api - now they are hidden
//@FilterDef(name = "deletedTaskFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
//@Filter(name = "deletedTaskFilter", condition = "deleted = :isDeleted")
@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@NamedNativeQueries({
        @NamedNativeQuery(
                name = Task.TasksByUserId,
                query = "select tasks.* from tasks inner join task_subscriber sub on tasks.id = sub.task_id where sub.subscriber_id = :user_id",
                resultClass = Task.class,
                hints = {
                        @QueryHint(name = QueryHints.READ_ONLY, value = "true"),
                        @QueryHint(name = QueryHints.CACHEABLE, value = "true")
                }
        )})
public class Task implements AbstractTask {

    public static final String TasksByUserId = "Tasks.TasksByUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "task_name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(max = 500)
    @Column(name = "task_description", length = 500)
    private String description;

    @NotNull
    @Column(name = "dateadded", nullable = false)
    private LocalDateTime dateAdded;

    @NotNull
    @Column(name = "task_status", nullable = false, columnDefinition = "SMALLINT NOT NULL")
    private Status status;

    @NotNull
    @Column(name = "priority", nullable = false, columnDefinition = "SMALLINT NOT NULL")
    private Priority priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @With
    @Builder.ObtainVia(method = "getTaskUserNested")
    private User user;

    @EqualsAndHashCode.Exclude
    @Builder.ObtainVia(method = "getTaskSubscribersNested")
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "task_subscriber",
            joinColumns = {@JoinColumn(name = "task_id")}
            , inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    private Set<User> subscribers = new HashSet<>();

    @Builder.Default
    @Column(name = "deleted", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted = false;

    @PrePersist
    private void prePersist() {
        this.dateAdded = LocalDateTime.now();
    }


    private User getTaskUserNested() {
        return !Objects.isNull(this.user) ? this.user.withTasks(Collections.emptyList()) : null;
    }

    private Set<User> getTaskSubscribersNested() {
        return Objects.isNull(subscribers) ? Collections.emptySet() : subscribers.stream()
                                                                                 .map(user -> user.withTasks(Collections.emptyList()))
                                                                                 .collect(Collectors.toSet());
    }

}


