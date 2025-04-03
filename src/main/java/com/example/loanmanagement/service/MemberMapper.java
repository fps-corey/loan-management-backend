package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.member.MemberDto;
import com.example.loanmanagement.dto.member.MemberDto.*;
import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

public class MemberMapper {

    public static MemberDto toDto(Member member) {
        List<LoanBriefDto> loanDtos = member.getLoans().stream()
                .map(MemberMapper::toLoanBriefDto)
                .collect(Collectors.toList());

        AddressDto addressDto = new AddressDto(
                member.getAddressStreet(),
                member.getAddressCity(),
                member.getAddressRegion(),
                member.getAddressPostalCode(),
                member.getAddressCountry()
        );

        EmploymentDto employmentDto = new EmploymentDto(
                member.getEmployerName(),
                member.getOccupation(),
                member.getMonthlyIncome() != null ? String.format("%.2f", member.getMonthlyIncome()) : null
        );

        AuditDto auditDto = new AuditDto(
                member.getCreatedAt(),
                member.getCreatedBy(),
                member.getUpdatedAt(),
                member.getLastModifiedBy()
        );

        return new MemberDto(
                member.getId(),
                member.getDisplayId(),
                member.getFirstName() + " " + member.getLastName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getDateOfBirth(),
                member.getGender(),
                member.getNationalId(),
                member.getCreditScore(),
                member.getDisplayId(), // assuming displayId doubles as member number
                "STANDARD", // or infer memberType from other fields
                member.isDocumentsVerified(),
                member.getCreatedAt().toLocalDate(),
                member.isActive() ? "ACTIVE" : "INACTIVE",
                addressDto,
                employmentDto,
                auditDto,
                loanDtos
        );
    }

    private static LoanBriefDto toLoanBriefDto(Loan loan) {
        return new LoanBriefDto(
                loan.getId(),
                loan.getLoanType(),
                loan.getAmount(),
                loan.getLoanPurpose(),
                loan.getStartDate(),
                loan.getTermInMonths(),
                loan.getStatus().name().toLowerCase()
        );
    }
}
