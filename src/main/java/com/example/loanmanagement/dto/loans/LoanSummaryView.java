package com.example.loanmanagement.dto.loans;

import java.util.UUID;

public interface LoanSummaryView {
    UUID getId();
    String getReferenceNumber();
    String getStatus();
    String getBorrower(); // custom field
}
