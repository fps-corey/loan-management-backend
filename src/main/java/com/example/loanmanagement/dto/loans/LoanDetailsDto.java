package com.example.loanmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class LoanDetailsDto {
    private UUID id;
    private UUID memberId;
    private String borrower;
    private String loanType;
    private BigDecimal existingAmount;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer term;
    private Integer numPayments;
    private String issueAccount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String additionalServices;
    private String loanPurpose;
    private String marketingCampaign;
    private BigDecimal fees;
    private BigDecimal totalLoanAmount;
    private String status;
    private List<LoanApprovalDto> approvals;

    // Getters and Setters omitted for brevity
}
