package com.example.loanmanagement.controller;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;
import com.example.loanmanagement.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    
    private final LoanService loanService;
    
    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody Loan loan) {
        return new ResponseEntity<>(loanService.createLoan(loan), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.getLoan(id));
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<Loan>> getMemberLoans(
            @PathVariable UUID memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanService.getMemberLoans(memberId, pageable));
    }
    
    @GetMapping
    public ResponseEntity<Page<Loan>> searchLoans(
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) String referenceNumber,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanService.searchLoans(memberId, status, referenceNumber, active, pageable));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(
            @PathVariable UUID id,
            @Valid @RequestBody Loan loan
    ) {
        return ResponseEntity.ok(loanService.updateLoan(id, loan));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable UUID id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateLoan(@PathVariable UUID id) {
        loanService.reactivateLoan(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/approve")
    public ResponseEntity<Loan> approveLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }
    
    @PostMapping("/{id}/reject")
    public ResponseEntity<Loan> rejectLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.rejectLoan(id));
    }
    
    @PostMapping("/{id}/activate")
    public ResponseEntity<Loan> activateLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.activateLoan(id));
    }
    
    @PostMapping("/{id}/close")
    public ResponseEntity<Loan> closeLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.closeLoan(id));
    }
    
    @PostMapping("/{id}/default")
    public ResponseEntity<Loan> defaultLoan(@PathVariable UUID id) {
        return ResponseEntity.ok(loanService.defaultLoan(id));
    }
    
    @PostMapping("/by-reference")
    public ResponseEntity<List<Loan>> findLoansByReferenceNumbers(@RequestBody List<String> referenceNumbers) {
        return ResponseEntity.ok(loanService.findLoansByReferenceNumbers(referenceNumbers));
    }
    
    @GetMapping("/active-by-payment/{amount}")
    public ResponseEntity<List<Loan>> findActiveLoansByPaymentAmount(@PathVariable Double amount) {
        return ResponseEntity.ok(loanService.findActiveLoansByPaymentAmount(amount));
    }
} 