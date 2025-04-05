package com.example.loanmanagement.entity;

import com.example.loanmanagement.entity.enums.DocumentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document extends BaseEntity {

    @Column(name = "display_id", unique = true, nullable = false)
    private String displayId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "content_type", nullable = false)
    private String contentType;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private Boolean signed = false;
    
    @Column(name = "signed_by")
    private String signedBy;
    
    @Column(name = "signed_at")
    private LocalDateTime signedAt;
    
    @Column(name = "signature_hash")
    private String signatureHash;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(nullable = false)
    private Boolean verified = false;
}