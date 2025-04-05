package com.example.loanmanagement.service.validation;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import org.springframework.stereotype.Component;

@Component
public class RequiredDocsValidator implements LoanStatusTransitionValidator {

    @Override
    public void validate(Loan loan, LoanStatus newStatus) {
//        if (newStatus == LoanStatus.PENDING_INTERVIEW && loan.getDocuments().isEmpty()) {
//            throw new IllegalStateException("Required documents must be uploaded before interview.");
//        }
    }
}
