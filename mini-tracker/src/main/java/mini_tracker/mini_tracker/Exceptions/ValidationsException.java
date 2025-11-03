package mini_tracker.mini_tracker.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class ValidationsException extends RuntimeException {


    private final List<String> errorsList;

    public ValidationsException(List<String> errorsList) {

        super("Errore di validazione nel payload");


        this.errorsList = errorsList;
    }
}