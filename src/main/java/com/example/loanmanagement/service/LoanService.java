package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.loans.LoanSummaryView;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.entity.enums.LoanStatus;
import com.example.loanmanagement.exception.NotFoundException;
import com.example.loanmanagement.repository.LoanRepository;
import com.example.loanmanagement.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanService {
    
    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;
    private final DisplayIdService displayIdService;
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);


    @Transactional
    public Loan createLoan(Loan loan) {
        // Verify member exists
        Member member = memberRepository.findById(loan.getMember().getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + loan.getMember().getId()));
        loan.setMember(member);
        loan.setDisplayId(displayIdService.getNextDisplayId("loan"));
        // Set initial status
        loan.setStatus(LoanStatus.REQUESTED);
        loan.setStartDate(LocalDate.now());
        loan.setActive(true);
        
        // Calculate end date based on term
        loan.setEndDate(loan.getStartDate().plusMonths(loan.getTermInMonths()));
        
        return loanRepository.save(loan);
    }
    
    public Loan getLoan(UUID id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Loan not found with id: " + id));
    }
    
    public Page<Loan> getMemberLoans(UUID memberId, Pageable pageable) {
        return loanRepository.findByMemberId(memberId, pageable);
    }
    
    public Page<Loan> searchLoans(
            UUID memberId,
            LoanStatus status,
            String referenceNumber,
            Boolean active,
            Pageable pageable
    ) {
        return loanRepository.findBySearchCriteria(memberId, status, referenceNumber, active, pageable);
    }
    
    @Transactional
    public Loan updateLoan(UUID id, Loan loan) {
        Loan existingLoan = getLoan(id);
        
        // Update fields
        existingLoan.setAmount(loan.getAmount());
        existingLoan.setInterestRate(loan.getInterestRate());
        existingLoan.setTermInMonths(loan.getTermInMonths());
        existingLoan.setMonthlyPayment(loan.getMonthlyPayment());
        existingLoan.setStatus(loan.getStatus());
        
        // Recalculate end date if term changed
        if (!existingLoan.getTermInMonths().equals(loan.getTermInMonths())) {
            existingLoan.setEndDate(existingLoan.getStartDate().plusMonths(loan.getTermInMonths()));
        }
        
        return loanRepository.save(existingLoan);
    }
    
    @Transactional
    public void deleteLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setActive(false);
        loanRepository.save(loan);
    }
    
    @Transactional
    public void reactivateLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setActive(true);
        loanRepository.save(loan);
    }
    
    @Transactional
    public Loan approveLoan(UUID id) {
        Loan loan = getLoan(id);
        if (loan.getApprovals().size() > 1) {
            loan.setStatus(LoanStatus.PENDING_COMMITTEE);
        } else {
            loan.setStatus(LoanStatus.PENDING_APPROVAL);
        }
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan rejectLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setStatus(LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan activateLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setStatus(LoanStatus.ACTIVE);
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan closeLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setStatus(LoanStatus.COMPLETE);
        return loanRepository.save(loan);
    }
    
    @Transactional
    public Loan defaultLoan(UUID id) {
        Loan loan = getLoan(id);
        loan.setStatus(LoanStatus.IN_ARREARS);
        return loanRepository.save(loan);
    }
    
    public List<Loan> findLoansByReferenceNumbers(List<String> referenceNumbers) {
        return loanRepository.findByReferenceNumbers(referenceNumbers);
    }
    
    public List<Loan> findActiveLoansByPaymentAmount(Double amount) {
        return loanRepository.findActiveLoansByPaymentAmount(amount);
    }

    public List<LoanSummaryView> findAllLoanSummaries() {
        return loanRepository.findAllLoanSummaries();
    }

    public LoanSummaryView findLoanSummaryById(UUID id) {
        try {
            Optional<LoanSummaryView> memberOpt = loanRepository.findLoanSummaryById(id);
            if (memberOpt.isEmpty()) {
                log.warn("No member found for id: {}", id);
                return null;
            }
            LoanSummaryView loan = memberOpt.get();
            System.out.println("SERVICE MEMBER: " + loan);
            return loan;
        } catch (Exception ex) {
            log.error("Error in getMember", ex);
            throw ex; // allow controller to log it too
        }
    }

    public List<LoanSummaryView> findLoanSummaryById(List<UUID> ids) {
        try {
            List<LoanSummaryView> loanSummariesByIds = loanRepository.findLoanSummariesByIds(ids);
            if (loanSummariesByIds.isEmpty()) {
                log.warn("No member found for id: {}", ids);
                return null;
            }
            return loanSummariesByIds;
        } catch (Exception ex) {
            log.error("Error in getMember", ex);
            throw ex; // allow controller to log it too
        }
    }
}