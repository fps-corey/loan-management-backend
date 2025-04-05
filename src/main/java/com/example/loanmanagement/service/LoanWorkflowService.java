package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.documents.UploadDocumentRequest;
import com.example.loanmanagement.entity.Document;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class LoanWorkflowService {

    private static final BigDecimal INTERVIEW_AMOUNT_TRIGGER = BigDecimal.valueOf(2000) ;
    private final DocumentService documentService;
    private final LoanService loanService;
    private final LoanStatusEngine loanStatusEngine;

    public LoanWorkflowService(DocumentService documentService, LoanService loanService, LoanStatusEngine loanStatusEngine) {
        this.documentService = documentService;
        this.loanService = loanService;
        this.loanStatusEngine = loanStatusEngine;
    }

    public Document uploadDocument(UploadDocumentRequest documentRequest) {
        Document uploadedDocument;
        try {
            uploadedDocument = documentService.uploadBase64Document(documentRequest);

            if (documentRequest.getLoan() != null) {
                Loan loan = loanService.getLoan(documentRequest.getLoan());
                // TODO verify ALL the documents have been uploaded
                // if ID, BANK STATEMENT AND OTHER TYPES ARE PRESENT FOR USER

                //IF OVER A CERTAIN AMOUNT OR CREDIT CHECK?
                if (loan.getTotalLoanAmount().compareTo(INTERVIEW_AMOUNT_TRIGGER) > 0) {
                    loanStatusEngine.transition(documentRequest.getLoan(),
                            LoanStatus.PENDING_INTERVIEW,
                            false,
                            "Required documents uploaded, but x triggers the interview");
                } else {
                    loanStatusEngine.transition(documentRequest.getLoan(),
                            LoanStatus.PENDING_APPROVAL,
                            false,
                            "Required documents uploaded, automatic approval of interview");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uploadedDocument;
    }


    public Loan createAndStartLoan(Loan loan) {
        Loan loanCreated = loanService.createLoan(loan);
        loanStatusEngine.transition(loan.getId(), LoanStatus.PENDING_DOCS, false, "Initial submission");
        return loan;
    }

    //TODO finalise and create flows
}
