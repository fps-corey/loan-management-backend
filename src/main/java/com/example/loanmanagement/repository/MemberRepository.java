package com.example.loanmanagement.repository;

import com.example.loanmanagement.dto.member.MemberSummaryDto;
import com.example.loanmanagement.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query(value = """
    SELECT 
      m.id AS id,
      m.display_id AS displayId,
      CONCAT(m.first_name, ' ', m.last_name) AS fullName,
      m.email AS email,
      m.phone_number AS phoneNumber,
      ARRAY_AGG(l.id) AS loanIds,
      COUNT(l.id) AS loanCount,
      m.active AS active
    FROM members m
    LEFT JOIN loans l ON m.id = l.member_id
    GROUP BY m.id, m.display_id, m.first_name, m.last_name, m.email, m.phone_number, m.active
""", nativeQuery = true)
    List<MemberSummaryDto> findAllSummaries();


    @Query(value = """
    SELECT 
      m.id AS id,
      m.display_id AS displayId,
      CONCAT(m.first_name, ' ', m.last_name) AS fullName,
      m.email AS email,
      m.phone_number AS phoneNumber,
      ARRAY_AGG(l.id) AS loanIds,
      COUNT(l.id) AS loanCount,
      m.active AS active
    FROM members m
    LEFT JOIN loans l ON m.id = l.member_id
    WHERE m.id = :id
    GROUP BY m.id, m.display_id, m.first_name, m.last_name, m.email, m.phone_number, m.active
""", nativeQuery = true)
    Optional<MemberSummaryDto> findSummaryById(@Param("id") UUID id);

    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
} 