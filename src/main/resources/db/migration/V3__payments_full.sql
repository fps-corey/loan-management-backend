-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
                                        id UUID PRIMARY KEY,
                                        loan_id UUID NOT NULL REFERENCES loans(id),
    amount NUMERIC(15, 2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    reference_number VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- BaseEntity fields
    version BIGINT NOT NULL DEFAULT 0,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    last_modified_by VARCHAR(100) NOT NULL DEFAULT 'system'
    );

-- Indexes
CREATE INDEX IF NOT EXISTS idx_payments_loan_id ON payments(loan_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);
CREATE INDEX IF NOT EXISTS idx_payments_payment_date ON payments(payment_date);

-- Mock data
INSERT INTO payments (
    id, loan_id, amount, payment_date, payment_method, reference_number, status, notes,
    active, version, deleted, created_at, updated_at, created_by, last_modified_by
) VALUES
      (
          gen_random_uuid(), (SELECT id FROM loans LIMIT 1),
    250.00, CURRENT_DATE - INTERVAL '15 days', 'BANK_TRANSFER', 'REF123456', 'COMPLETED', 'First installment',
    true, 0, false, now(), now(), 'system', 'system'
    ),
    (
    gen_random_uuid(), (SELECT id FROM loans OFFSET 1 LIMIT 1),
    100.00, CURRENT_DATE - INTERVAL '10 days', 'CASH', 'REF789012', 'PENDING', 'Pending verification',
    true, 0, false, now(), now(), 'admin', 'admin'
    );
