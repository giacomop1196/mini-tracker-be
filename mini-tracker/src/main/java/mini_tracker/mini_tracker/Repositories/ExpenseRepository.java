package mini_tracker.mini_tracker.Repositories;

import mini_tracker.mini_tracker.Entities.Expense;
import mini_tracker.mini_tracker.Entities.User;
import mini_tracker.mini_tracker.Enums.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findByUser(User user);

    Page<Expense> findByUser_UserId(UUID userId, Pageable pageable);

    List<Expense> findByType(ExpenseType type);

    List<Expense> findByDate(LocalDate date);
}