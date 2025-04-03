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

    public MemberDto(UUID id, String displayId, String fullName, String email, String phoneNumber, LocalDate dateOfBirth, String gender, String nationalId, Integer creditScore, String memberNumber, String memberType, boolean documentsVerified, LocalDate joinedDate, String status, AddressDto address, EmploymentDto employment, AuditDto audit, List<LoanBriefDto> loans) {
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

    public UUID getId() { return id; }
    public String getDisplayId() { return displayId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getNationalId() { return nationalId; }
    public Integer getCreditScore() { return creditScore; }
    public String getMemberNumber() { return memberNumber; }
    public String getMemberType() { return memberType; }
    public boolean isDocumentsVerified() { return documentsVerified; }
    public LocalDate getJoinedDate() { return joinedDate; }
    public String getStatus() { return status; }
    public AddressDto getAddress() { return address; }
    public EmploymentDto getEmployment() { return employment; }
    public AuditDto getAudit() { return audit; }
    public List<LoanBriefDto> getLoans() { return loans; }

    public static class AddressDto {
        private String street, city, region, postalCode, country;
        public AddressDto(String street, String city, String region, String postalCode, String country) {
            this.street = street;
            this.city = city;
            this.region = region;
            this.postalCode = postalCode;
            this.country = country;
        }
        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getRegion() { return region; }
        public String getPostalCode() { return postalCode; }
        public String getCountry() { return country; }
    }

    public static class EmploymentDto {
        private String employerName, occupation, incomeBracket;
        public EmploymentDto(String employerName, String occupation, String incomeBracket) {
            this.employerName = employerName;
            this.occupation = occupation;
            this.incomeBracket = incomeBracket;
        }
        public String getEmployerName() { return employerName; }
        public String getOccupation() { return occupation; }
        public String getIncomeBracket() { return incomeBracket; }
    }

    public static class AuditDto {
        private LocalDateTime createdAt, updatedAt;
        private String createdBy, lastModifiedBy;
        public AuditDto(LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String lastModifiedBy) {
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.updatedAt = updatedAt;
            this.lastModifiedBy = lastModifiedBy;
        }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public String getCreatedBy() { return createdBy; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public String getLastModifiedBy() { return lastModifiedBy; }
    }

    public static class LoanBriefDto {
        private UUID id;
        private String displayId;
        private String loanType;
        private BigDecimal amount;
        private String loanPurpose;
        private LocalDate startDate;
        private Integer term;
        private String status;
        public LoanBriefDto(UUID id, String displayId, String loanType, BigDecimal amount, String loanPurpose, LocalDate startDate, Integer term, String status) {
            this.id = id;
            this.displayId = displayId;
            this.loanType = loanType;
            this.amount = amount;
            this.loanPurpose = loanPurpose;
            this.startDate = startDate;
            this.term = term;
            this.status = status;
        }
        public UUID getId() { return id; }
        public String getDisplayId() { return displayId; }
        public String getLoanType() { return loanType; }
        public BigDecimal getAmount() { return amount; }
        public String getLoanPurpose() { return loanPurpose; }
        public LocalDate getStartDate() { return startDate; }
        public Integer getTerm() { return term; }
        public String getStatus() { return status; }
    }
}
