package com.example.loanmanagement.dto.loans;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class LoanDto {

    private UUID id;
    private String displayId;
    private UUID memberId;
    private String borrower;

    private String loanType;
    private String loanPurpose;
    private String marketingCampaign;
    private String additionalServices;

    private BigDecimal amount;
    private BigDecimal existingAmount;
    private BigDecimal fees;
    private BigDecimal totalLoanAmount;
    private BigDecimal interestRate;

    private Integer term;
    private Integer numPayments;
    private String issueAccount;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status;

    public static LoanDto from(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId());
        dto.setDisplayId(loan.getDisplayId());
        dto.setMemberId(loan.getMember().getId());
        dto.setBorrower(loan.getMember().getFirstName() + " " + loan.getMember().getLastName());

        dto.setLoanType(loan.getLoanType());
        dto.setLoanPurpose(loan.getLoanPurpose());
        dto.setMarketingCampaign(loan.getMarketingCampaign());
        dto.setAdditionalServices(loan.getAdditionalServices());

        dto.setAmount(loan.getAmount());
        dto.setExistingAmount(loan.getExistingAmount());
        dto.setFees(loan.getFees());
        dto.setTotalLoanAmount(loan.getTotalLoanAmount());
        dto.setInterestRate(loan.getInterestRate());

        dto.setTerm(loan.getTermInMonths());
        dto.setNumPayments(loan.getNumPayments());
        dto.setIssueAccount(loan.getIssueAccount());

        dto.setStartDate(loan.getStartDate());
        dto.setEndDate(loan.getEndDate());

        dto.setStatus(loan.getStatus().name().toLowerCase());

        return dto;
    }

    // Getters & Setters

    public UUID getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public String getMarketingCampaign() {
        return marketingCampaign;
    }

    public void setMarketingCampaign(String marketingCampaign) {
        this.marketingCampaign = marketingCampaign;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExistingAmount() {
        return existingAmount;
    }

    public void setExistingAmount(BigDecimal existingAmount) {
        this.existingAmount = existingAmount;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public BigDecimal getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public void setTotalLoanAmount(BigDecimal totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getNumPayments() {
        return numPayments;
    }

    public void setNumPayments(Integer numPayments) {
        this.numPayments = numPayments;
    }

    public String getIssueAccount() {
        return issueAccount;
    }

    public void setIssueAccount(String issueAccount) {
        this.issueAccount = issueAccount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
