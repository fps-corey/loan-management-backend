package com.example.loanmanagement.service;

import com.example.loanmanagement.entity.Document;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.entity.enums.DocumentType;
import com.example.loanmanagement.repository.DocumentRepository;
import com.example.loanmanagement.repository.LoanRepository;
import com.example.loanmanagement.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    
    private static final String UPLOAD_DIR = "/app/uploads/documents";
    
    @Transactional
    public Document uploadDocument(
            MultipartFile file,
            UUID memberId,
            UUID loanId,
            DocumentType type,
            String description
    ) throws IOException {
        // Verify member exists
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        
        // Verify loan exists if provided
        Loan loan = null;
        if (loanId != null) {
            loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new EntityNotFoundException("Loan not found with id: " + loanId));
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);
        
        // Get latest version number
        Optional<Integer> latestVersion = documentRepository.findLatestVersionByMemberAndType(memberId, type);
        int version = latestVersion.map(v -> v + 1).orElse(1);
        
        // Create document entity
        Document document = new Document();
        document.setMember(member);
        document.setLoan(loan);
        document.setFileName(originalFilename);
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setFilePath(filePath.toString());
        document.setDocumentType(type);
        document.setVersionNumber(version);
        document.setDescription(description);
        document.setActive(true);
        
        return documentRepository.save(document);
    }
    
    public Document getDocument(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
    }
    
    public byte[] downloadDocument(UUID id) throws IOException {
        Document document = getDocument(id);
        Path filePath = Paths.get(document.getFilePath());
        return Files.readAllBytes(filePath);
    }
    
    public Page<Document> getMemberDocuments(UUID memberId, Pageable pageable) {
        return documentRepository.findByMemberId(memberId, pageable);
    }
    
    public Page<Document> getLoanDocuments(UUID loanId, Pageable pageable) {
        return documentRepository.findByLoanId(loanId, pageable);
    }
    
    public Page<Document> searchDocuments(
            UUID memberId,
            UUID loanId,
            DocumentType type,
            String fileName,
            Boolean active,
            Pageable pageable
    ) {
        return documentRepository.findBySearchCriteria(memberId, loanId, type, fileName, active, pageable);
    }
    
    @Transactional
    public Document updateDocument(UUID id, String description) {
        Document document = getDocument(id);
        document.setDescription(description);
        return documentRepository.save(document);
    }
    
    @Transactional
    public void deleteDocument(UUID id) throws IOException {
        Document document = getDocument(id);
        
        // Delete file from filesystem
        Path filePath = Paths.get(document.getFilePath());
        Files.deleteIfExists(filePath);
        
        // Soft delete document
        document.setActive(false);
        documentRepository.save(document);
    }
    
    @Transactional
    public Document signDocument(UUID id, String signedBy, String signatureHash) {
        Document document = getDocument(id);
        document.setSignedBy(signedBy);
        document.setSignedAt(LocalDateTime.now());
        document.setSignatureHash(signatureHash);
        document.setVerified(true);
        return documentRepository.save(document);
    }
    
    public List<Document> getLatestVersionsByMemberAndType(UUID memberId, DocumentType type) {
        return documentRepository.findLatestVersionsByMemberAndType(memberId, type);
    }
    
    public List<Document> getSignedAgreementsByLoan(UUID loanId) {
        return documentRepository.findSignedAgreementsByLoan(loanId);
    }
} 