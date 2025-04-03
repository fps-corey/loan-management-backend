package com.example.loanmanagement.dto;

import com.example.loanmanagement.entity.Loan;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoanSummaryDto {
    private UUID id;
    private String borrower;
    private BigDecimal amount;
    private String status;
    private String loanInMonths;

    public static LoanSummaryDto from(Loan loan) {
        LoanSummaryDto dto = new LoanSummaryDto();
        dto.setId(loan.getId());

        if (loan.getMember() != null) {
            String first = loan.getMember().getFirstName();
            String last = loan.getMember().getLastName();
            dto.setBorrower(first + " " + last);
        } else {
            dto.setBorrower("Unknown");
        }

        dto.setAmount(loan.getAmount());
        dto.setStatus(loan.getStatus().name());
        dto.setLoanInMonths(loan.getTermInMonths() + " months");

        return dto;
    }

    public static List<LoanSummaryDto> fromList(List<Loan> loans) {
        return loans.stream()
                .map(LoanSummaryDto::from)
                .collect(Collectors.toList());
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanInMonths() {
        return loanInMonths;
    }

    public void setLoanInMonths(String loanInMonths) {
        this.loanInMonths = loanInMonths;
    }
}
