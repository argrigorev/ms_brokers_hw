package ru.netology.creditApi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplicationRequest {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal income;
    private BigDecimal currentCreditLoad;
    private Integer creditRating;
}

