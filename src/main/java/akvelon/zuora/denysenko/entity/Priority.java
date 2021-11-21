package akvelon.zuora.denysenko.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.NoSuchElementException;

/**
 * @author Denysenko Stanislav
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Priority {

    LOW(0),
    MEDIUM(1),
    HIGH(2),
    CRITICAL(3);

    private final short priorityIndex;

    Priority(int priorityIndex) {
        this.priorityIndex = (short) priorityIndex;
    }

    public static Priority getPriority(int value) {
        for (Priority p : values()) {
            if (p.priorityIndex == value)
                return p;
        }
        throw new NoSuchElementException();
    }


    public short getPriorityIndex() {
        return priorityIndex;
    }

    @Override
    public String toString() {
        return "Priority{" +
                "priorityIndex=" + priorityIndex +
                "}";
    }

}
