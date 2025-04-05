package com.example.loanmanagement.controller;

import com.example.loanmanagement.dto.documents.DocumentDto;
import com.example.loanmanagement.dto.documents.DocumentView;
import com.example.loanmanagement.dto.documents.UploadDocumentRequest;
import com.example.loanmanagement.entity.Document;
import com.example.loanmanagement.entity.enums.DocumentType;
import com.example.loanmanagement.repository.DocumentRepository;
import com.example.loanmanagement.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    
    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    @PostMapping("/base64")
    public ResponseEntity<DocumentDto> uploadBase64Document(@RequestBody UploadDocumentRequest request) throws IOException {
        Document saved = documentService.uploadBase64Document(request);
        return ResponseEntity.ok(new DocumentDto(saved));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<DocumentView>> getDocumentSummary() {
        List<DocumentView> top20Docs =
                documentRepository.findAllViews(PageRequest.of(0, 20)).getContent();

        return ResponseEntity.ok(top20Docs);
    }

    @GetMapping("/member/summary/{id}")
    public ResponseEntity<List<DocumentView>> getDocumentSummaryByMemberId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(documentRepository.findAllViewsByMemberId(id));
    }

    @GetMapping("/loans/summary/{id}")
    public ResponseEntity<List<DocumentView>> getDocumentSummaryByLoanId(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(documentRepository.findAllViewsByLoanId(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(new DocumentDto(documentService.getDocument(id)));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable UUID id) throws Exception {
        Document document = documentService.getDocument(id);
        byte[] data = documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<Page<Document>> getMemberDocuments(
            @PathVariable UUID memberId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.getMemberDocuments(memberId, pageable));
    }
    
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<Page<Document>> getLoanDocuments(
            @PathVariable UUID loanId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.getLoanDocuments(loanId, pageable));
    }
    
    @GetMapping
    public ResponseEntity<Page<Document>> searchDocuments(
            @RequestParam(required = false) UUID memberId,
            @RequestParam(required = false) UUID loanId,
            @RequestParam(required = false) DocumentType type,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return ResponseEntity.ok(documentService.searchDocuments(memberId, loanId, type, fileName, active, pageable));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody String description
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, description));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) throws Exception {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/sign")
    public ResponseEntity<Document> signDocument(
            @PathVariable UUID id,
            @RequestParam String signedBy,
            @RequestParam String signatureHash
    ) {
        return ResponseEntity.ok(documentService.signDocument(id, signedBy, signatureHash));
    }
    
    @GetMapping("/member/{memberId}/latest-versions")
    public ResponseEntity<List<Document>> getLatestVersionsByMemberAndType(
            @PathVariable UUID memberId,
            @RequestParam DocumentType type
    ) {
        return ResponseEntity.ok(documentService.getLatestVersionsByMemberAndType(memberId, type));
    }
    
    @GetMapping("/loan/{loanId}/signed-agreements")
    public ResponseEntity<List<Document>> getSignedAgreementsByLoan(@PathVariable UUID loanId) {
        return ResponseEntity.ok(documentService.getSignedAgreementsByLoan(loanId));
    }
} 