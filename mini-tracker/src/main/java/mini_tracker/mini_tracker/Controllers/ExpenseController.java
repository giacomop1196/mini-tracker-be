package mini_tracker.mini_tracker.Controllers;

import jakarta.validation.Valid;
import mini_tracker.mini_tracker.Entities.Expense;
import mini_tracker.mini_tracker.Payloads.ExpensePayload;
import mini_tracker.mini_tracker.Services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user/{userId}/expense")
@Validated
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // CREARE UNA NUOVA SPESA per un utente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense createNewExpense(
            @PathVariable UUID userId,
            @RequestBody @Valid ExpensePayload payload) {
        return expenseService.saveNewExpense(payload, userId);
    }

    // OTTENERE TUTTE LE SPESE di un utente (paginato)
    @GetMapping
    public Page<Expense> getUserExpenses(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort
    ) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];

        Sort.Direction direction = Sort.Direction.ASC;
        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("DESC")) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, property));
        return expenseService.findByUserId(userId, pageable);
    }

    // OTTENERE UNA SPESA SINGOLA
    @GetMapping("/{expenseId}")
    public Expense getSingleExpense(
            @PathVariable UUID userId,
            @PathVariable UUID expenseId) {

        return expenseService.findById(expenseId, userId);
    }

    // MODIFICARE UNA SPESA
    @PutMapping("/{expenseId}")
    public Expense updateExpense(
            @PathVariable UUID userId,
            @PathVariable UUID expenseId,
            @RequestBody @Valid ExpensePayload payload) {
        return expenseService.findByIdAndUpdate(expenseId, payload, userId);
    }

    // CANCELLARE UNA SPESA
    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(
            @PathVariable UUID userId,
            @PathVariable UUID expenseId) {
        expenseService.findByIdAndDelete(expenseId, userId);
    }
}