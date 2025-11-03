package mini_tracker.mini_tracker.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal; // Importa BigDecimal per la valuta
import java.time.LocalDate;  // Importa LocalDate per la data
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(lombok.AccessLevel.NONE)
    private UUID revenueId;

    private LocalDate date;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private User user;

    public Revenue(LocalDate date, BigDecimal amount, User user) {
        this.date = date;
        this.amount = amount;
        this.user = user;
    }
}