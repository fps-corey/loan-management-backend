package com.example.loanmanagement.service.validation;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;

public interface LoanStatusTransitionValidator {
    void validate(Loan loan, LoanStatus newStatus);
}
