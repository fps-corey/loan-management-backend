package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.BankStatementParseResult;
import com.example.loanmanagement.dto.BankTransaction;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import com.example.loanmanagement.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankStatementProcessingService {

    private final LoanRepository loanRepository;
    private static final Pattern REFERENCE_PATTERN = Pattern.compile("L-\\d{4}-\\d{3}");

    public BankStatementParseResult processStatement(MultipartFile file) throws IOException {
        String content = extractTextFromPDF(file);
        BankStatementParseResult result = parseStatementContent(content);
        matchTransactions(result);
        return result;
    }

    private String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private BankStatementParseResult parseStatementContent(String content) {
        List<BankTransaction> transactions = new ArrayList<>();
        LocalDate startDate = null;
        LocalDate endDate = null;
        BigDecimal openingBalance = null;
        BigDecimal closingBalance = null;

        // Example patterns - adjust based on actual bank statement format
        Pattern datePattern = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        Pattern transactionPattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s+([\\w\\s]+)\\s+([\\-\\+]?\\d+\\.\\d{2})");
        Pattern balancePattern = Pattern.compile("Balance: \\$(\\d+\\.\\d{2})");

        String[] lines = content.split("\\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        for (String line : lines) {
            // Extract dates
            Matcher dateMatcher = datePattern.matcher(line);
            if (dateMatcher.find()) {
                LocalDate date = LocalDate.parse(dateMatcher.group(), formatter);
                if (startDate == null || date.isBefore(startDate)) {
                    startDate = date;
                }
                if (endDate == null || date.isAfter(endDate)) {
                    endDate = date;
                }
            }

            // Extract transactions
            Matcher transactionMatcher = transactionPattern.matcher(line);
            if (transactionMatcher.find()) {
                LocalDate date = LocalDate.parse(transactionMatcher.group(1), formatter);
                String description = transactionMatcher.group(2).trim();
                BigDecimal amount = new BigDecimal(transactionMatcher.group(3));
                String referenceNumber = extractReferenceNumber(description);

                transactions.add(new BankTransaction(
                    date,
                    description,
                    amount,
                    amount.compareTo(BigDecimal.ZERO) > 0 ? "CREDIT" : "DEBIT",
                    referenceNumber,
                    null,
                    null,
                    null
                ));
            }

            // Extract balances
            Matcher balanceMatcher = balancePattern.matcher(line);
            if (balanceMatcher.find()) {
                BigDecimal balance = new BigDecimal(balanceMatcher.group(1));
                if (openingBalance == null) {
                    openingBalance = balance;
                }
                closingBalance = balance;
            }
        }

        return new BankStatementParseResult(
            startDate,
            endDate,
            openingBalance != null ? openingBalance : BigDecimal.ZERO,
            closingBalance != null ? closingBalance : BigDecimal.ZERO,
            transactions
        );
    }

    private void matchTransactions(BankStatementParseResult result) {
        // Get all active loans for matching
        List<Loan> activeLoans = loanRepository.findByStatus(LoanStatus.ACTIVE);
        Map<String, Loan> loansByReference = activeLoans.stream()
                .collect(Collectors.toMap(Loan::getReferenceNumber, loan -> loan));

        for (BankTransaction transaction : result.getTransactions()) {
            if (transaction.getReferenceNumber() != null && 
                REFERENCE_PATTERN.matcher(transaction.getReferenceNumber()).matches()) {
                
                Loan matchedLoan = loansByReference.get(transaction.getReferenceNumber());
                if (matchedLoan != null) {
                    if (matchedLoan.getMonthlyPayment().compareTo(transaction.getAmount()) == 0) {
                        transaction.setConfidence(BankTransaction.MatchConfidence.EXACT);
                        transaction.setMatchReason("Reference number and amount match");
                        transaction.setMatchedLoanId(matchedLoan.getId().toString());
                    } else {
                        transaction.setConfidence(BankTransaction.MatchConfidence.HIGH);
                        transaction.setMatchReason("Reference number matches but amount differs");
                        transaction.setMatchedLoanId(matchedLoan.getId().toString());
                    }
                    continue;
                }
            }

            // Check for amount-based matches
            boolean foundMatch = false;
            for (Loan loan : activeLoans) {
                if (loan.getMonthlyPayment().compareTo(transaction.getAmount()) == 0) {
                    transaction.setConfidence(BankTransaction.MatchConfidence.MEDIUM);
                    transaction.setMatchReason("Amount matches loan " + loan.getReferenceNumber());
                    transaction.setMatchedLoanId(loan.getId().toString());
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                transaction.setConfidence(BankTransaction.MatchConfidence.LOW);
                transaction.setMatchReason("No direct matches found");
            }
        }
    }

    private String extractReferenceNumber(String description) {
        Matcher matcher = REFERENCE_PATTERN.matcher(description);
        return matcher.find() ? matcher.group() : null;
    }
} 