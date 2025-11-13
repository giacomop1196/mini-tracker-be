package mini_tracker.mini_tracker.Controllers;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import lombok.ToString;
import mini_tracker.mini_tracker.Entities.Expense;
import mini_tracker.mini_tracker.Entities.Revenue;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Payloads.UserPayload;
import mini_tracker.mini_tracker.Payloads.UserUpdatePayload;
import mini_tracker.mini_tracker.Repositories.UserRepository;
import mini_tracker.mini_tracker.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    //Utenti Paginati
    @GetMapping
    public Page<User> getTuttiUtenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "email") String sort
    ) {
        return userService.getAllUsers(page, size, sort);
    }

    //Utente Singolo
    @GetMapping("/{utenteId}")
    public User getUtenteById(@PathVariable UUID utenteId){

        return userService.findById(utenteId);

    }

    //Creazione Utente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createNewUtente(@RequestBody UserPayload payload) {
        return userService.saveNewUser(payload);
    }

    //Rimozione Utente
    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtente(@PathVariable("utenteId") UUID utenteId) { // Usa "utenteId"
        userService.findByIdAndDelete(utenteId);
    }

    @PutMapping("/{utenteId}")
    public User updateUtente(
            @PathVariable("utenteId") UUID utenteId,
            @RequestBody @Valid UserUpdatePayload payload) {
        return userService.findByIdAndUpdate(utenteId, payload);
    }

    // Blocca utente
    @PatchMapping("/{userId}/lock")
    public User lockUser(@PathVariable UUID userId) {
        return userService.lockUser(userId);
    }

    // Sblocca utente
    @PatchMapping("/{userId}/unlock")
    public User unlockUser(@PathVariable UUID userId) {
        return userService.unlockUser(userId);
    }

    @GetMapping("/stats/total")
    public Map<String, Long> getTotalUserStats() {
        return Map.of("totalUsers", userService.getTotalUserCount());
    }

    @GetMapping("/stats/locked")
    public Map<String, Long> getLockedUserStats() {
        return Map.of("totalLocked", userService.getLockedUserCount());
    }

    @GetMapping("/stats/global-economy")
    public Map<String, BigDecimal> getGlobalEconomyStats() {
        return Map.of(
                "globalRevenue", userService.getGlobalTotalRevenue(),
                "globalExpenses", userService.getGlobalTotalExpenses()
        );
    }

}
