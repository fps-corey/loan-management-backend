package com.example.loanmanagement.dto.member;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MemberDto {
    private UUID id;
    private String displayId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationalId;
    private Integer creditScore;
    private String memberNumber;
    private String memberType;
    private boolean documentsVerified;
    private LocalDate joinedDate;
    private String status;
    private AddressDto address;
    private EmploymentDto employment;
    private AuditDto audit;
    private List<LoanBriefDto> loans;

    public MemberDto(UUID id,
                     String displayId,
                     String fullName,
                     String email,
                     String phoneNumber,
                     LocalDate dateOfBirth,
                     String gender,
                     String nationalId,
                     Integer creditScore,
                     String memberNumber,
                     String memberType,
                     boolean documentsVerified,
                     LocalDate joinedDate,
                     String status,
                     AddressDto address,
                     EmploymentDto employment,
                     AuditDto audit,
                     List<LoanBriefDto> loans) {
        this.id = id;
        this.displayId = displayId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nationalId = nationalId;
        this.creditScore = creditScore;
        this.memberNumber = memberNumber;
        this.memberType = memberType;
        this.documentsVerified = documentsVerified;
        this.joinedDate = joinedDate;
        this.status = status;
        this.address = address;
        this.employment = employment;
        this.audit = audit;
        this.loans = loans;
    }

    // Getters and setters omitted for brevity — include them if needed

    public static class AddressDto {
        private String street;
        private String city;
        private String region;
        private String postalCode;
        private String country;

        public AddressDto(String street, String city, String region, String postalCode, String country) {
            this.street = street;
            this.city = city;
            this.region = region;
            this.postalCode = postalCode;
            this.country = country;
        }

        // Getters and setters if needed
    }

    public static class EmploymentDto {
        private String employerName;
        private String occupation;
        private String incomeBracket;

        public EmploymentDto(String employerName, String occupation, String incomeBracket) {
            this.employerName = employerName;
            this.occupation = occupation;
            this.incomeBracket = incomeBracket;
        }

        // Getters and setters if needed
    }

    public static class AuditDto {
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String lastModifiedBy;

        public AuditDto(LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String lastModifiedBy) {
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.updatedAt = updatedAt;
            this.lastModifiedBy = lastModifiedBy;
        }

        // Getters and setters if needed
    }

    public static class LoanBriefDto {
        private UUID id;
        private String loanType;
        private BigDecimal amount;
        private String loanPurpose;
        private LocalDate startDate;
        private Integer term;
        private String status;

        public LoanBriefDto(UUID id, String loanType, BigDecimal amount, String loanPurpose, LocalDate startDate, Integer term, String status) {
            this.id = id;
            this.loanType = loanType;
            this.amount = amount;
            this.loanPurpose = loanPurpose;
            this.startDate = startDate;
            this.term = term;
            this.status = status;
        }

        // Getters and setters if needed
    }
}
