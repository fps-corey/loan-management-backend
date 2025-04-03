package com.example.loanmanagement.controller;

import com.example.loanmanagement.dto.loans.LoanSummaryView;
import com.example.loanmanagement.dto.member.MemberDto;
import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.entity.Member;
import com.example.loanmanagement.service.MemberMapper;
import com.example.loanmanagement.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    @PostMapping
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {
        return new ResponseEntity<>(memberService.createMember(member), HttpStatus.CREATED);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<MemberSummaryDto>> getLoanSummaries() {
        return ResponseEntity.ok(memberService.getMemberSummary());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getMember(@PathVariable UUID id) {
        return ResponseEntity.ok(MemberMapper.toDto(memberService.getMember(id)));
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
    public ResponseEntity<Member> updateMember(
            @PathVariable UUID id,
            @Valid @RequestBody Member member
    ) {
        return ResponseEntity.ok(memberService.updateMember(id, member));
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