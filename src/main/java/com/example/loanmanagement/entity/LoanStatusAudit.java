package com.example.loanmanagement.entity;

import com.example.loanmanagement.entity.enums.LoanStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan_status_audit")
public class LoanStatusAudit {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus toStatus;

    @Column(nullable = false)
    private String performedBy;

    @Column(nullable = false)
    private Instant performedAt;

    @Column(nullable = false)
    private boolean override;

    @Column(length = 1000)
    private String reason;

    public LoanStatusAudit() {}

    public LoanStatusAudit(Loan loan, LoanStatus fromStatus, LoanStatus toStatus, String performedBy,
                           Instant performedAt, boolean override, String reason) {
        this.loan = loan;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.performedBy = performedBy;
        this.performedAt = performedAt;
        this.override = override;
        this.reason = reason;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LoanStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(LoanStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public LoanStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(LoanStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public Instant getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Instant performedAt) {
        this.performedAt = performedAt;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
