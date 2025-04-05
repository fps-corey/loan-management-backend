package com.example.loanmanagement.dto.documents;

import java.time.LocalDateTime;

public interface DocumentView {
    String getId();
    String getDisplayId();
    String getName();             // fileName
    String getType();             // documentType
    String getMember();           // member.displayId
    String getLoan();             // loan.displayId
    LocalDateTime getUploaded();  // createdAt
    String getVersion();          // version as string (if needed)
    boolean isRequiresSignature();
    String getMimeType();
}
