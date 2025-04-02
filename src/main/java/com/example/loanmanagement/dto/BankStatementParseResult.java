package com.example.loanmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankStatementParseResult {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private List<BankTransaction> transactions;
} 