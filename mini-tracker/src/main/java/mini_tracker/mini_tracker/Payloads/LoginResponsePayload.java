package mini_tracker.mini_tracker.Payloads;
import java.util.UUID;

public record LoginResponsePayload(String token, UUID userId) {
}