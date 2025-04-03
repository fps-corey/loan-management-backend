package com.example.loanmanagement.entity;

import com.example.loanmanagement.entity.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class Loan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String referenceNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer termInMonths;

    @Column(nullable = false)
    private BigDecimal monthlyPayment;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    @Column(nullable = false)
    private boolean active = true;

    @Column
    private BigDecimal existingAmount;

    @Column
    private BigDecimal fees;

    @Column
    private BigDecimal totalLoanAmount;

    @Column
    private String issueAccount;

    @Column
    private Integer numPayments;

    @Column
    private String loanType;

    @Column
    private String loanPurpose;

    @Column
    private String marketingCampaign;

    @Column
    private String additionalServices;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LoanApproval> approvals = new HashSet<>();
}
