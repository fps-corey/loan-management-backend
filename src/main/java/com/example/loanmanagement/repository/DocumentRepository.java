package com.example.loanmanagement.repository;

import com.example.loanmanagement.dto.documents.DocumentView;
import com.example.loanmanagement.entity.Document;
import com.example.loanmanagement.entity.enums.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    
    Page<Document> findByMemberId(UUID memberId, Pageable pageable);
    
    Page<Document> findByLoanId(UUID loanId, Pageable pageable);
    
    @Query("SELECT d FROM Document d WHERE " +
           "(:memberId IS NULL OR d.member.id = :memberId) AND " +
           "(:loanId IS NULL OR d.loan.id = :loanId) AND " +
           "(:type IS NULL OR d.documentType = :type) AND " +
           "(:fileName IS NULL OR LOWER(d.fileName) LIKE LOWER(CONCAT('%', :fileName, '%'))) AND " +
           "(:active IS NULL OR d.active = :active)")
    Page<Document> findBySearchCriteria(
            @Param("memberId") UUID memberId,
            @Param("loanId") UUID loanId,
            @Param("type") DocumentType type,
            @Param("fileName") String fileName,
            @Param("active") Boolean active,
            Pageable pageable
    );
    
    @Query("SELECT d FROM Document d WHERE d.member.id = :memberId AND d.documentType = :type ORDER BY d.version DESC")
    List<Document> findLatestVersionsByMemberAndType(
            @Param("memberId") UUID memberId,
            @Param("type") DocumentType type
    );
    
    @Query("SELECT MAX(d.version) FROM Document d WHERE d.member.id = :memberId AND d.documentType = :type")
    Optional<Integer> findLatestVersionByMemberAndType(
            @Param("memberId") UUID memberId,
            @Param("type") DocumentType type
    );
    
    @Query("SELECT d FROM Document d WHERE d.loan.id = :loanId AND d.documentType = 'SIGNED_AGREEMENT' ORDER BY d.version DESC")
    List<Document> findSignedAgreementsByLoan(@Param("loanId") UUID loanId);

    @Query("""
    SELECT 
        d.id AS id,
        d.displayId AS displayId,
        d.fileName AS name,
        d.documentType AS type,
        m.displayId AS member,
        l.displayId AS loan,
        d.createdAt AS uploaded,
        CONCAT('v', d.version) AS version,
        NOT d.signed AS requiresSignature,
        d.contentType AS mimeType
    FROM Document d
    JOIN d.member m
    LEFT JOIN d.loan l
    WHERE d.member.id = :memberId
""")
    List<DocumentView> findAllViewsByMemberId(@Param("memberId") UUID memberId);

    @Query("""
    SELECT 
        d.id AS id,
        d.displayId AS displayId,
        d.fileName AS name,
        d.documentType AS type,
        m.displayId AS member,
        l.displayId AS loan,
        d.createdAt AS uploaded,
        CONCAT('v', d.version) AS version,
        NOT d.signed AS requiresSignature,
        d.contentType AS mimeType
    FROM Document d
    JOIN d.member m
    LEFT JOIN d.loan l
    WHERE d.loan.id = :loanId
""")
    List<DocumentView> findAllViewsByLoanId(@Param("loanId") UUID loanId);


    @Query("""
    SELECT 
        d.id AS id,
        d.displayId AS displayId,
        d.fileName AS name,
        d.documentType AS type,
        m.displayId AS member,
        l.displayId AS loan,
        d.createdAt AS uploaded,
        CONCAT('v', d.version) AS version,
        NOT d.signed AS requiresSignature,
        d.contentType AS mimeType
    FROM Document d
    JOIN d.member m
    LEFT JOIN d.loan l
""")
    Page<DocumentView> findAllViews(Pageable pageable);

} 