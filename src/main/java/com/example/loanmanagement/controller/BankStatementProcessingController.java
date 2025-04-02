package com.example.loanmanagement.controller;

import com.example.loanmanagement.dto.BankStatementParseResult;
import com.example.loanmanagement.service.BankStatementProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/bank-statements")
@RequiredArgsConstructor
public class BankStatementProcessingController {

    private final BankStatementProcessingService bankStatementProcessingService;

    @PostMapping("/process")
    public ResponseEntity<BankStatementParseResult> processStatement(
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(bankStatementProcessingService.processStatement(file));
    }
} 