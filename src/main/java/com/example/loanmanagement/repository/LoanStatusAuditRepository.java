package com.example.loanmanagement.repository;

import com.example.loanmanagement.entity.LoanStatusAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanStatusAuditRepository extends JpaRepository<LoanStatusAudit, Long> {
}
