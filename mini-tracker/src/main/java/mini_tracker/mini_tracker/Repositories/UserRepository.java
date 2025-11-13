package mini_tracker.mini_tracker.Repositories;

import mini_tracker.mini_tracker.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository <User, UUID> {
    Optional<User> findByEmail(String email);
    long countByLocked(boolean locked);
}
