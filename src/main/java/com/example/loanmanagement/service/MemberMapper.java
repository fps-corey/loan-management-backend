package com.example.loanmanagement.service;

import com.example.loanmanagement.entity.Loan;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.dto.member.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MemberMapper {

    public static MemberDto toDto(Member member) {
        List<MemberDto.LoanBriefDto> loanDtos = member.getLoans().stream()
                .map(MemberMapper::toLoanBriefDto)
                .sorted(Comparator.comparing(MemberDto.LoanBriefDto::getStartDate).reversed())
                .collect(Collectors.toList());

        MemberDto.AddressDto addressDto = new MemberDto.AddressDto(
                member.getAddressStreet(),
                member.getAddressCity(),
                member.getAddressRegion(),
                member.getAddressPostalCode(),
                member.getAddressCountry()
        );

        MemberDto.EmploymentDto employmentDto = new MemberDto.EmploymentDto(
                member.getEmployerName(),
                member.getOccupation(),
                member.getMonthlyIncome() != null ? String.format("%.2f", member.getMonthlyIncome()) : null
        );

        MemberDto.AuditDto auditDto = new MemberDto.AuditDto(
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
                member.isActive() ? "active" : "inactive",
                addressDto,
                employmentDto,
                auditDto,
                loanDtos
        );
    }

    private static MemberDto.LoanBriefDto toLoanBriefDto(Loan loan) {
        return new MemberDto.LoanBriefDto(
                loan.getId(),
                loan.getDisplayId(),
                loan.getLoanType(),
                loan.getAmount(),
                loan.getLoanPurpose(),
                loan.getStartDate(),
                loan.getTermInMonths(),
                loan.getStatus().name().toLowerCase()
        );
    }
}
