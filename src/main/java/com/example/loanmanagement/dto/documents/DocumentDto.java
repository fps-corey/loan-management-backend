package com.example.loanmanagement.dto.documents;

import com.example.loanmanagement.entity.Document;
import com.example.loanmanagement.entity.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentDto {
    private String id;
    private String name;
    private String type;
    private String member;
    private String loan;
    private LocalDateTime uploaded;
    private String version;
    private boolean requiresSignature;
    private String contentBase64;
    private String mimeType;

    public DocumentDto(Document doc) {
        this.id = doc.getId().toString();
        this.name = doc.getFileName();
        this.type = doc.getDocumentType().name();
        this.member = doc.getMember().getDisplayId();
        this.loan = doc.getLoan() != null ? doc.getLoan().getDisplayId() : null;
        this.uploaded = doc.getCreatedAt();
        this.version = "v" + doc.getVersion();
        this.requiresSignature = !Boolean.TRUE.equals(doc.getSigned());
        this.mimeType = doc.getContentType();

        // Optional: read content from disk and encode
        try {
            byte[] bytes = Files.readAllBytes(Path.of(doc.getFilePath()));
            this.contentBase64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            this.contentBase64 = null; // silently fail, or log
        }
    }

    public DocumentDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public LocalDateTime getUploaded() {
        return uploaded;
    }

    public void setUploaded(LocalDateTime uploaded) {
        this.uploaded = uploaded;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRequiresSignature() {
        return requiresSignature;
    }

    public void setRequiresSignature(boolean requiresSignature) {
        this.requiresSignature = requiresSignature;
    }

    public String getContentBase64() {
        return contentBase64;
    }

    public void setContentBase64(String contentBase64) {
        this.contentBase64 = contentBase64;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
