package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    
    private final MemberRepository memberRepository;
    private DisplayIdService displayIdService;
    
    @Transactional
    public Member createMember(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (memberRepository.existsByPhoneNumber(member.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        member.setDisplayId(displayIdService.getNextDisplayId("member"));
        return memberRepository.save(member);
    }
    
    public Member getMember(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
    }
    
    public Page<Member> searchMembers(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            Boolean status,
            Pageable pageable
    ) {
        return memberRepository.findBySearchCriteria(firstName, lastName, email, phoneNumber, status, pageable);
    }
    
    @Transactional
    public Member updateMember(UUID id, Member member) {
        Member existingMember = getMember(id);
        
        // Check if email is being changed and if it's already taken
        if (!existingMember.getEmail().equals(member.getEmail()) &&
                memberRepository.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Check if phone number is being changed and if it's already taken
        if (!existingMember.getPhoneNumber().equals(member.getPhoneNumber()) &&
                memberRepository.existsByPhoneNumber(member.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        
        // Update fields
        existingMember.setFirstName(member.getFirstName());
        existingMember.setLastName(member.getLastName());
        existingMember.setEmail(member.getEmail());
        existingMember.setPhoneNumber(member.getPhoneNumber());
        existingMember.setDateOfBirth(member.getDateOfBirth());
        existingMember.setMaritalStatus(member.getMaritalStatus());
        existingMember.setEducationLevel(member.getEducationLevel());
        existingMember.setResidentialStatus(member.getResidentialStatus());
        existingMember.setNumberOfDependents(member.getNumberOfDependents());
        existingMember.setOccupation(member.getOccupation());
        existingMember.setEmployerName(member.getEmployerName());
        existingMember.setMonthlyIncome(member.getMonthlyIncome());
        existingMember.setBankName(member.getBankName());
        existingMember.setBankAccountNumber(member.getBankAccountNumber());
        existingMember.setBankSortCode(member.getBankSortCode());
        existingMember.setNextOfKinName(member.getNextOfKinName());
        existingMember.setNextOfKinPhone(member.getNextOfKinPhone());
        existingMember.setNextOfKinRelationship(member.getNextOfKinRelationship());
        existingMember.setSpouseName(member.getSpouseName());
        existingMember.setSpouseOccupation(member.getSpouseOccupation());
        existingMember.setSpouseMonthlyIncome(member.getSpouseMonthlyIncome());
        existingMember.setPreferredLanguage(member.getPreferredLanguage());
        existingMember.setMembershipChannel(member.getMembershipChannel());
        existingMember.setReferralSource(member.getReferralSource());
        
        return memberRepository.save(existingMember);
    }
    
    @Transactional
    public void deleteMember(UUID id) {
        Member member = getMember(id);
        member.setActive(false);
        memberRepository.save(member);
    }
    
    @Transactional
    public void reactivateMember(UUID id) {
        Member member = getMember(id);
        member.setActive(true);
        memberRepository.save(member);
    }

    public List<MemberSummaryDto> getMemberSummary() {
        return memberRepository.findAllSummaries();
    }
}