package akvelon.zuora.denysenko.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.NoSuchElementException;

/**
 * @author Denysenko Stanislav
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {

    TODO(0),
    IN_PROGRESS(1),
    READY(2),
    COMPLETED(3);

    private final short statusIndex;

    Status(int statusIndex) {
        this.statusIndex = (short) statusIndex;
    }

    public static Status getStatus(int value) {
        for (Status s : values()) {
            if (s.statusIndex == value)
                return s;
        }
        throw new NoSuchElementException();
    }

    public short getStatusIndex() {
        return statusIndex;
    }

    @Override
    public String toString() {
        return "Status{" +
                "statusIndex=" + statusIndex +
                "}";
    }

}
