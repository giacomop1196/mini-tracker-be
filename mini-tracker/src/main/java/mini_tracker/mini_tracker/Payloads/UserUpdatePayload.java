package mini_tracker.mini_tracker.Payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Payload specifico per l'aggiornamento, senza password
public record UserUpdatePayload(
        @NotBlank
        @Size(min = 3, max = 20, message = "username deve avere almeno 3 caratteri e max 20")
        String username,
        @NotBlank
        String email,
        @NotBlank
        @Size(min = 3, max = 20, message = "nome deve avere almeno 3 caratteri e max 20")
        String name,
        @NotBlank
        @Size(min = 3, max = 20, message = "cognome deve avere almeno 3 caratteri e max 20")
        String surname
) {
}