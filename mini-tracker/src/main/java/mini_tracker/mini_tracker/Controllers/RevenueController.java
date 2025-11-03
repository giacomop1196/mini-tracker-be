package mini_tracker.mini_tracker.Controllers;

import jakarta.validation.Valid;
import mini_tracker.mini_tracker.Entities.Revenue;
import mini_tracker.mini_tracker.Payloads.RevenuePayload;
import mini_tracker.mini_tracker.Services.RevenueService;
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
@RequestMapping("/user/{userId}/revenue")
@Validated
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    // CREARE UNA NUOVA ENTRATA per un utente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Revenue createNewRevenue(
            @PathVariable UUID userId,
            @RequestBody @Valid RevenuePayload payload) {
        return revenueService.saveNewRevenue(payload, userId);
    }

    // OTTENERE TUTTE LE ENTRATE di un utente (paginato)
    @GetMapping
    public Page<Revenue> getUserRevenues(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return revenueService.findByUserId(userId, pageable);
    }

    // OTTENERE UNA ENTRATA SINGOLA
    @GetMapping("/{revenueId}")
    public Revenue getSingleRevenue(
            @PathVariable UUID userId,
            @PathVariable UUID revenueId) {
        return revenueService.findById(revenueId, userId);
    }

    // MODIFICARE UNA ENTRATA
    @PutMapping("/{revenueId}")
    public Revenue updateRevenue(
            @PathVariable UUID userId,
            @PathVariable UUID revenueId,
            @RequestBody @Valid RevenuePayload payload) {
        return revenueService.findByIdAndUpdate(revenueId, payload, userId);
    }

    // CANCELLARE UNA ENTRATA
    @DeleteMapping("/{revenueId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRevenue(
            @PathVariable UUID userId,
            @PathVariable UUID revenueId) {
        revenueService.findByIdAndDelete(revenueId, userId);
    }
}