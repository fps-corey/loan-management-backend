package com.example.loanmanagement.dto.loans;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanDto {
    private UUID id;
    private BigDecimal amount;
    private LoanStatus status;
    private UUID memberId;

    public static LoanDto from(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId());
        dto.setAmount(loan.getAmount());
        dto.setStatus(loan.getStatus());
        dto.setMemberId(loan.getMember().getId());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }
}