package mini_tracker.mini_tracker.Exceptions;

import java.util.List;

public class ValidationsException extends RuntimeException {
    public ValidationsException(List<String> message) {
        super((Throwable) message);
    }
}
