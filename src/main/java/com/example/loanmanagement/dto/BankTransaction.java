package com.example.loanmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransaction {
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private String type; // CREDIT or DEBIT
    private String referenceNumber;
    private MatchConfidence confidence;
    private String matchReason;
    private String matchedLoanId;

    public enum MatchConfidence {
        EXACT,      // Perfect reference number match
        HIGH,       // Amount and date match with loan payment
        MEDIUM,     // Amount matches but date is off
        LOW        // Only amount matches
    }
} 