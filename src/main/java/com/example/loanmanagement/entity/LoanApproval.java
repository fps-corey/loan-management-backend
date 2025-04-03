package com.example.loanmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "loan_approvals")
@Getter
@Setter
public class LoanApproval extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private String approver;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String decision;

    @Column
    private String notes;
}
