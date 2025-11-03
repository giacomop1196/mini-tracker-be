package mini_tracker.mini_tracker.Payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenuePayload(
        @NotNull(message = "La data è obbligatoria")
        @PastOrPresent(message = "La data non può essere nel futuro")
        LocalDate date,

        @NotNull(message = "L'importo è obbligatorio")
        @Positive(message = "L'importo deve essere positivo")
        BigDecimal amount
) {
}