package com.example.loanmanagement.repository;

import com.example.loanmanagement.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    
    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByPhoneNumber(String phoneNumber);
    
    @Query("SELECT m FROM Member m WHERE " +
           "(:firstName IS NULL OR LOWER(m.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
           "(:lastName IS NULL OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
           "(:email IS NULL OR LOWER(m.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:phoneNumber IS NULL OR m.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND " +
           "(:status IS NULL OR m.active = :status)")
    Page<Member> findBySearchCriteria(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") Boolean status,
            Pageable pageable
    );
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
} 