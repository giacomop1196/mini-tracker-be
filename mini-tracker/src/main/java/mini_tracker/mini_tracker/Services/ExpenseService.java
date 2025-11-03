package mini_tracker.mini_tracker.Services;

import lombok.extern.slf4j.Slf4j;
import mini_tracker.mini_tracker.Entities.Expense;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.ForbiddenException;
import mini_tracker.mini_tracker.Exceptions.NotFoundException;
import mini_tracker.mini_tracker.Payloads.ExpensePayload;
import mini_tracker.mini_tracker.Repositories.ExpenseRepository;
import mini_tracker.mini_tracker.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    // SALVARE UNA NUOVA SPESA
    public Expense saveNewExpense(ExpensePayload payload, UUID userId) {
        // Troviamo l'utente
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato", userId));

        Expense newExpense = new Expense(
                payload.date(),
                payload.amount(),
                payload.type(),
                user
        );

        Expense savedExpense = expenseRepository.save(newExpense);
        log.info("Spesa " + savedExpense.getExpenseId() + " salvata per l'utente " + user.getSurname());
        return savedExpense;
    }

    // OTTENERE TUTTE LE SPESE DI UN UTENTE (PAGINATE)
    public Page<Expense> findByUserId(UUID userId, Pageable pageable) {
        // Verifichiamo prima che l'utente esista
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Utente non trovato", userId);
        }
        return expenseRepository.findByUser_UserId(userId, pageable);
    }

    // TROVARE UNA SPESA SINGOLA
    public Expense findById(UUID expenseId, UUID userId) {
        Expense found = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Spesa non trovata", expenseId));

        // Verifichiamo che la spesa appartenga all'utente che la sta chiedendo
        if (!found.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Non hai il permesso di visualizzare questa spesa.");
        }
        return found;
    }

    // MODIFICARE UNA SPESA
    public Expense findByIdAndUpdate(UUID expenseId, ExpensePayload payload, UUID userId) {

        Expense found = this.findById(expenseId, userId);
        found.setDate(payload.date());
        found.setAmount(payload.amount());
        found.setType(payload.type());
        return expenseRepository.save(found);
    }

    // CANCELLARE UNA SPESA
    public void findByIdAndDelete(UUID expenseId, UUID userId) {
        Expense found = this.findById(expenseId, userId);
        expenseRepository.delete(found);
        log.info("Spesa con ID " + expenseId + " eliminata correttamente.");
    }
}