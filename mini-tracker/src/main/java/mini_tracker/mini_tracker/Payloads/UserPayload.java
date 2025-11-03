package mini_tracker.mini_tracker.Payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPayload(
        @NotBlank
        @Size(min = 3, max = 20, message = "username deve avere almeno 3 caratteri e max 20")
        String username,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        @Size(min = 3, max = 20, message = "nome deve avere almeno 3 caratteri e max 20")
        String name,
        @NotBlank
        @Size(min = 3, max = 20, message = "cognome deve avere almeno 3 caratteri e max 20")
        String surname
) {

}
