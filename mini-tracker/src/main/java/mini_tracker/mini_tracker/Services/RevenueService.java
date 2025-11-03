package mini_tracker.mini_tracker.Services;

import lombok.extern.slf4j.Slf4j;
import mini_tracker.mini_tracker.Entities.Revenue;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Exceptions.ForbiddenException;
import mini_tracker.mini_tracker.Exceptions.NotFoundException;
import mini_tracker.mini_tracker.Payloads.RevenuePayload;
import mini_tracker.mini_tracker.Repositories.RevenueRepository;
import mini_tracker.mini_tracker.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private UserRepository userRepository;

    // SALVARE UNA NUOVA ENTRATA
    public Revenue saveNewRevenue(RevenuePayload payload, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato", userId));

        Revenue newRevenue = new Revenue(
                payload.date(),
                payload.amount(),
                user
        );

        Revenue savedRevenue = revenueRepository.save(newRevenue);
        log.info("Entrata " + savedRevenue.getRevenueId() + " salvata per l'utente " + user.getSurname());
        return savedRevenue;
    }

    // OTTENERE TUTTE LE ENTRATE DI UN UTENTE (paginate)
    public Page<Revenue> findByUserId(UUID userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Utente non trovato", userId);
        }
        return revenueRepository.findByUser_UserId(userId, pageable);
    }

    // TROVARE UNA ENTRATA SINGOLA
    public Revenue findById(UUID revenueId, UUID userId) {
        Revenue found = revenueRepository.findById(revenueId)
                .orElseThrow(() -> new NotFoundException("Entrata non trovata", revenueId));
        if (!found.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException("Non hai il permesso di visualizzare questa entrata.");
        }
        return found;
    }

    // MODIFICARE UN ENTRATA
    public Revenue findByIdAndUpdate(UUID revenueId, RevenuePayload payload, UUID userId) {

        Revenue found = this.findById(revenueId, userId);

        found.setDate(payload.date());
        found.setAmount(payload.amount());

        return revenueRepository.save(found);
    }

    // CANCELLARE UN ENTRATA
    public void findByIdAndDelete(UUID revenueId, UUID userId) {
        Revenue found = this.findById(revenueId, userId);
        revenueRepository.delete(found);
        log.info("Entrata con ID " + revenueId + " eliminata correttamente.");
    }
}