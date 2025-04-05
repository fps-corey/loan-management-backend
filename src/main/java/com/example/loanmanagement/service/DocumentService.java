package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.documents.UploadDocumentRequest;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Base64;
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
    public Document uploadBase64Document(UploadDocumentRequest request) throws IOException {
        Member member = memberRepository.findById(request.getMember())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + request.getMember()));

        Loan loan = null;
        if (request.getLoan() != null) {
            loan = loanRepository.findById(request.getLoan())
                    .orElseThrow(() -> new EntityNotFoundException("Loan not found with id: " + request.getLoan()));
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileExtension = ".bin";
        if (request.getName() != null && request.getName().contains(".")) {
            fileExtension = request.getName().substring(request.getName().lastIndexOf("."));
        }

        String uniqueFilename = UUID.randomUUID() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Decode base64
        String base64Data = request.getContentBase64();
        if (base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

        Files.write(filePath, decodedBytes, StandardOpenOption.CREATE);

        DocumentType type = DocumentType.valueOf(request.getType());
        Optional<Integer> latestVersion = documentRepository.findLatestVersionByMemberAndType(request.getMember(), type);
        int version = latestVersion.map(v -> v + 1).orElse(1);

        Document document = new Document();
        document.setDisplayId("DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        document.setMember(member);
        document.setLoan(loan);
        document.setFileName(request.getName());
        document.setContentType(request.getMimeType());
        document.setFileSize((long) decodedBytes.length);
        document.setFilePath(filePath.toString());
        document.setDocumentType(type);
        document.setVersion((long) version);
        document.setDescription(request.getDescription());
        document.setSigned(false);
        document.setVerified(false);
        document.setActive(true);
        document.setCreatedAt(LocalDateTime.now());
        document.setCreatedBy("ADMIN"); //TODO get username or simialr from token
        document.setLastModifiedBy("ADMIN");
        document.setDeleted(false);
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
        Path filePath = Paths.get(document.getFilePath());
        Files.deleteIfExists(filePath);
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
