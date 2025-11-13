package mini_tracker.mini_tracker.Repositories;

import mini_tracker.mini_tracker.Entities.Revenue;
import mini_tracker.mini_tracker.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, UUID> {

    List<Revenue> findByUser(User user);

    List<Revenue> findByUser_UserId(UUID userId);

    List<Revenue> findByDate(LocalDate date);

    Page<Revenue> findByUser_UserId(UUID userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Revenue r")
    BigDecimal getGlobalTotalRevenue();
}