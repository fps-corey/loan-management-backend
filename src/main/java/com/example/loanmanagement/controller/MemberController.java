package com.example.loanmanagement.controller;

import com.example.loanmanagement.dto.loans.LoanSummaryView;
import com.example.loanmanagement.dto.member.MemberDto;
import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.dto.member.MemberUpdateDto;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.exception.GlobalExceptionHandler;
import com.example.loanmanagement.service.MemberMapper;
import com.example.loanmanagement.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        return new ResponseEntity<>(memberService.createMember(member), HttpStatus.CREATED);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<MemberSummaryDto>> getMemberSummaries() {
        return ResponseEntity.ok(memberService.getMemberSummary());
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<MemberSummaryDto> getMemberSummaryId(@PathVariable UUID id) {
        return ResponseEntity.ok(memberService.getMemberSummaryById(id));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDto> getMember(@PathVariable UUID id) {
        try {
            Member member = memberService.getMember(id);  // this is failing
            System.out.println("THIS IS MEMBER: " + member);
            log.info("retrieved member: {}", member);
            MemberDto dto = MemberMapper.toDto(member);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            log.error("Exception in controller", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<Page<Member>> searchMembers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Boolean status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(memberService.searchMembers(firstName, lastName, email, phoneNumber, status, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(
            @PathVariable UUID id,
            @RequestBody MemberUpdateDto dto
    ) {
        memberService.updateMember(id, dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable UUID id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateMember(@PathVariable UUID id) {
        memberService.reactivateMember(id);
        return ResponseEntity.ok().build();
    }
} 