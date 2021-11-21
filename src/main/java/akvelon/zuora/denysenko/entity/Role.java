package akvelon.zuora.denysenko.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.NoSuchElementException;

/**
 * Role can give rights, and data presentations for group of users.
 *
 * @author Denysenko Stanislav
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

    ADMIN(0),
    USER(1),
    SUPERVISOR(2),
    DEVELOPER(3);

    private final short roleIndex;

    Role(int statusIndex) {
        this.roleIndex = (short) statusIndex;
    }

    public static Role getRole(int value) {
        for (Role s : values()) {
            if (s.roleIndex == value)
                return s;
        }
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleIndex=" + roleIndex +
                '}';
    }
}
