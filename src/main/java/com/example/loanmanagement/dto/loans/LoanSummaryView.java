package com.example.loanmanagement.dto.loans;

import java.util.UUID;

public interface LoanSummaryView {
    UUID getId();
    String getDisplayId();
    String getReferenceNumber();
    String getStatus();
    String getMemberId();
    String getTotalAmount();
    String getTermInMonths();
    String getBorrower(); // custom field
}
