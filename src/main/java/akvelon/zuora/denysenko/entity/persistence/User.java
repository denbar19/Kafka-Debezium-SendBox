package akvelon.zuora.denysenko.entity.persistence;

import akvelon.zuora.denysenko.entity.AbstractUser;
import akvelon.zuora.denysenko.entity.Role;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User entity. User have his {@code tasks} that he was assigned to do.
 * And have {@code subscriptions} that he subscribed to track activity.
 * <p>dateAdded is set in {@code @PrePersist} method
 *
 * @author Denysenko Stanislav
 */
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
// if i need to get "deleted" entities via api - now they are hidden
//@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
//@Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User implements AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "user_name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @NotBlank
    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @NotNull
    @Column(name = "dateadded", nullable = false)
    private LocalDateTime dateAdded;

    @NotNull
    @Column(name = "user_role", nullable = false, columnDefinition = "SMALLINT NOT NULL")
    private Role role;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
    @ToString.Exclude()
    @With
    @Builder.ObtainVia(method = "getUserTasksNested")
    private List<Task> tasks;

    @EqualsAndHashCode.Exclude
    @ManyToMany(
            mappedBy = "subscribers",
            fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @ToString.Exclude()
    @With
    @Builder.ObtainVia(method = "getUserSubscriptionsNested")
    private Set<Task> subscriptions;

    @Builder.Default
    @Column(name="deleted", nullable = false, columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean deleted = false;

    @PreRemove
    private void preRemove() {
        if (!Objects.isNull(this.tasks)) {
            tasks.forEach(task -> task.setUser(null));
        }
    }

    @PrePersist
    private void prePersist() {
        this.dateAdded = LocalDateTime.now();
    }

    public String getNameSurname() {
        return surname + " " + name;
    }

    private List<Task> getUserTasksNested() {
        return Objects.isNull(tasks) ? null : tasks.stream().
                map(task -> task.withUser(null)).collect(Collectors.toList());
    }

    private Set<Task> getUserSubscriptionsNested() {
        return Objects.isNull(subscriptions) ? null : subscriptions.stream().
                map(task -> task.withUser(null)).collect(Collectors.toSet());
    }
}
