package com.example.loanmanagement.dto.member;

import java.util.List;
import java.util.UUID;

public interface MemberSummaryDto {
    UUID getId();
    String getDisplayId();
    String getFullName();
    String getEmail();
    String getPhoneNumber();
    List<UUID> getLoanIds();
    int getLoanCount();
    boolean isActive();
}
