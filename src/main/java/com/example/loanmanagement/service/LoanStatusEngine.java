package com.example.loanmanagement.service;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import com.example.loanmanagement.exception.InvalidLoanTransitionException;
import com.example.loanmanagement.repository.LoanRepository;
import com.example.loanmanagement.entity.LoanStatusAudit;
import com.example.loanmanagement.repository.LoanStatusAuditRepository;
import com.example.loanmanagement.service.validation.LoanStatusTransitionValidator;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class LoanStatusEngine {

    private final LoanRepository loanRepository;
    private final LoanStatusAuditRepository auditRepository;
    private final List<LoanStatusTransitionValidator> validators;

    public LoanStatusEngine(LoanRepository loanRepository,
                            LoanStatusAuditRepository auditRepository,
                            List<LoanStatusTransitionValidator> validators) {
        this.loanRepository = loanRepository;
        this.auditRepository = auditRepository;
        this.validators = validators;
    }

    private static final Map<LoanStatus, Set<LoanStatus>> validTransitions = Map.ofEntries(
            Map.entry(LoanStatus.REQUESTED, Set.of(LoanStatus.PENDING_DOCS, LoanStatus.REJECTED)),
            Map.entry(LoanStatus.PENDING_DOCS, Set.of(LoanStatus.PENDING_INTERVIEW, LoanStatus.PENDING_APPROVAL,LoanStatus.REJECTED)),
            Map.entry(LoanStatus.PENDING_INTERVIEW, Set.of(LoanStatus.PENDING_COMMITTEE, LoanStatus.PENDING_APPROVAL, LoanStatus.REJECTED)),
            Map.entry(LoanStatus.PENDING_COMMITTEE, Set.of(LoanStatus.PENDING_APPROVAL, LoanStatus.REJECTED)),
            Map.entry(LoanStatus.PENDING_APPROVAL, Set.of(LoanStatus.AWAITING_PAYOUT, LoanStatus.REJECTED)),
            Map.entry(LoanStatus.AWAITING_PAYOUT, Set.of(LoanStatus.PAYOUT_CONFIRMED, LoanStatus.REJECTED)),
            Map.entry(LoanStatus.PAYOUT_CONFIRMED, Set.of(LoanStatus.ACTIVE)),
            Map.entry(LoanStatus.ACTIVE, Set.of(LoanStatus.IN_ARREARS, LoanStatus.COMPLETE)),
            Map.entry(LoanStatus.IN_ARREARS, Set.of(LoanStatus.ACTIVE, LoanStatus.COMPLETE)),
            Map.entry(LoanStatus.COMPLETE, Set.of()),
            Map.entry(LoanStatus.REJECTED, Set.of())
    );

    @Transactional
    public Loan transition(UUID loanId, LoanStatus newStatus, boolean override, String reason) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        LoanStatus currentStatus = loan.getStatus();

        if (!override && !validTransitions.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new InvalidLoanTransitionException(currentStatus.name(), newStatus.name());
        }

        if (!override) {
            validators.forEach(validator -> validator.validate(loan, newStatus));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String performedBy;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            performedBy = jwtAuth.getToken().getSubject(); // gets the "sub" claim
        } else {
            performedBy = authentication.getName(); // fallback
        }
        loan.setStatus(newStatus);
        loanRepository.save(loan);

        auditRepository.save(new LoanStatusAudit(
                loan,
                currentStatus,
                newStatus,
                performedBy,
                Instant.now(),
                override,
                reason
        ));
        return loan;
    }
}
