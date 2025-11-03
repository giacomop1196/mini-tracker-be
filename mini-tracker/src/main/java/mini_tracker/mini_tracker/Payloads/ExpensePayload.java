package mini_tracker.mini_tracker.Payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent; // Utile per le date
import jakarta.validation.constraints.Positive;     // Utile per gli importi
import mini_tracker.mini_tracker.Enums.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpensePayload(
        @NotNull(message = "La data è obbligatoria")
        @PastOrPresent(message = "La data non può essere nel futuro")
        LocalDate date,

        @NotNull(message = "L'importo è obbligatorio")
        @Positive(message = "L'importo deve essere positivo")
        BigDecimal amount,

        @NotNull(message = "Il tipo di spesa è obbligatorio")
        ExpenseType type
) {
}