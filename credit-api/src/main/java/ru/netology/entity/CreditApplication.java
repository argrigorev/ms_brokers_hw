package ru.netology.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.model.ApplicationStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal income;
    private BigDecimal currentCreditLoad;
    private Integer creditRating;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
