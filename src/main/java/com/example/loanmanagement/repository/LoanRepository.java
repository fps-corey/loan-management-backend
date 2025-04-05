package com.example.loanmanagement.repository;

import com.example.loanmanagement.dto.loans.LoanSummaryView;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    
    Page<Loan> findByMemberId(UUID memberId, Pageable pageable);
    
    Optional<Loan> findByReferenceNumber(String referenceNumber);
    
    List<Loan> findByStatus(LoanStatus status);
    
    @Query("SELECT l FROM Loan l WHERE " +
           "(:memberId IS NULL OR l.member.id = :memberId) AND " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:referenceNumber IS NULL OR l.referenceNumber LIKE CONCAT('%', :referenceNumber, '%')) AND " +
           "(:active IS NULL OR l.active = :active)")
    Page<Loan> findBySearchCriteria(
            @Param("memberId") UUID memberId,
            @Param("status") LoanStatus status,
            @Param("referenceNumber") String referenceNumber,
            @Param("active") Boolean active,
            Pageable pageable
    );
    
    @Query("SELECT l FROM Loan l WHERE l.referenceNumber IN :referenceNumbers")
    List<Loan> findByReferenceNumbers(@Param("referenceNumbers") List<String> referenceNumbers);
    
    @Query("SELECT l FROM Loan l WHERE l.monthlyPayment = :amount AND l.status = 'ACTIVE'")
    List<Loan> findActiveLoansByPaymentAmount(@Param("amount") Double amount);

    @Query("""
    SELECT
        l.id AS id,
        l.displayId as displayId,
        l.referenceNumber AS referenceNumber,
        l.status AS status,
        l.amount AS totalAmount,
        l.termInMonths AS termInMonths,
        CONCAT(m.firstName, ' ', m.lastName) AS borrower
    FROM Loan l
    JOIN l.member m
    """)
    List<LoanSummaryView> findAllLoanSummaries();

    @Query("""
    SELECT
        l.id AS id,
        l.displayId as displayId,
        l.referenceNumber AS referenceNumber,
        l.status AS status,
        l.amount AS totalAmount,
        l.termInMonths AS termInMonths,
        CONCAT(m.firstName, ' ', m.lastName) AS borrower
    FROM Loan l
    JOIN l.member m
    WHERE l.id = :id
""")
    Optional<LoanSummaryView> findLoanSummaryById(@Param("id") UUID id);

    @Query("""
    SELECT
        l.id AS id,
        l.displayId as displayId,
        l.referenceNumber AS referenceNumber,
        l.status AS status,
        l.amount AS totalAmount,
        l.termInMonths AS termInMonths,
        CONCAT(m.firstName, ' ', m.lastName) AS borrower
    FROM Loan l
    JOIN l.member m
    WHERE l.id IN :ids
""")
    List<LoanSummaryView> findLoanSummariesByIds(@Param("ids") List<UUID> ids);

} 