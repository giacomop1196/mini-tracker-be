package mini_tracker.mini_tracker.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mini_tracker.mini_tracker.Enums.ExpenseType; // Importa il tuo Enum

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(lombok.AccessLevel.NONE)
    private UUID expenseId;

    private LocalDate date;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Expense(LocalDate date, BigDecimal amount, ExpenseType type, User user) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.user = user;
    }
}