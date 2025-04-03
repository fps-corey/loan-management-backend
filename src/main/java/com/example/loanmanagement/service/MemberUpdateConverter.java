package com.example.loanmanagement.service.converter;

import com.example.loanmanagement.dto.member.MemberUpdateDto;
import com.example.loanmanagement.entity.Member;

public final class MemberUpdateConverter {

    private MemberUpdateConverter() {
        // Utility class – no instances
    }

    public static void applyUpdate(MemberUpdateDto dto, Member member) {
        if (dto.getEmail() != null) {
            member.setEmail(dto.getEmail());
        }

        if (dto.getPhoneNumber() != null) {
            member.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getAddress() != null) {
            var address = dto.getAddress();
            if (address.getStreet() != null) {
                member.setAddressStreet(address.getStreet());
            }
            if (address.getCity() != null) {
                member.setAddressCity(address.getCity());
            }
            if (address.getRegion() != null) {
                member.setAddressRegion(address.getRegion());
            }
            if (address.getPostalCode() != null) {
                member.setAddressPostalCode(address.getPostalCode());
            }
        }

        if (dto.getEmployment() != null) {
            var employment = dto.getEmployment();
            if (employment.getEmployerName() != null) {
                member.setEmployerName(employment.getEmployerName());
            }
            if (employment.getOccupation() != null) {
                member.setOccupation(employment.getOccupation());
            }
            if (employment.getIncomeBracket() != null) {
                try {
                    member.setMonthlyIncome(Double.parseDouble(employment.getIncomeBracket()));
                } catch (NumberFormatException ignored) {
                    // Optionally log this
                }
            }
        }
    }
}
