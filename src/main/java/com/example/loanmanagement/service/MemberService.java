package com.example.loanmanagement.service;

import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.dto.member.MemberUpdateDto;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.loanmanagement.service.converter.MemberUpdateConverter.applyUpdate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    
    private final MemberRepository memberRepository;
    private DisplayIdService displayIdService;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

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
        try {
            Optional<Member> memberOpt = memberRepository.findById(id);
            if (memberOpt.isEmpty()) {
                log.warn("No member found for id: {}", id);
                return null;
            }
            Member member = memberOpt.get();
            System.out.println("SERVICE MEMBER: " + member);
            return member;
        } catch (Exception ex) {
            log.error("Error in getMember", ex);
            throw ex; // allow controller to log it too
        }
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
    public Member updateMember(UUID id, MemberUpdateDto dto) {
        Member existing = getMember(id);

        // Fail early if email or phone taken
        if (!existing.getEmail().equals(dto.getEmail()) &&
                memberRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!existing.getPhoneNumber().equals(dto.getPhoneNumber()) &&
                memberRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        applyUpdate(dto, existing);

        return memberRepository.save(existing);
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