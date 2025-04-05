CREATE TABLE loan_status_audit (
                                   id UUID PRIMARY KEY,
                                   loan_id UUID NOT NULL,
                                   from_status VARCHAR(50) NOT NULL,
                                   to_status VARCHAR(50) NOT NULL,
                                   performed_by VARCHAR(255) NOT NULL,
                                   performed_at TIMESTAMP NOT NULL,
                                   override BOOLEAN NOT NULL,
                                   reason VARCHAR(1000),

                                   CONSTRAINT fk_loan FOREIGN KEY (loan_id) REFERENCES loans(id) ON DELETE CASCADE
);