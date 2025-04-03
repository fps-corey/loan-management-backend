
-- V1__init_schema.sql (combined and with indexes)

-- MEMBERS
CREATE TABLE members (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT,
    deleted BOOLEAN NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(255) UNIQUE NOT NULL,
    date_of_birth DATE NOT NULL,
    address TEXT NOT NULL,
    marital_status VARCHAR(50) NOT NULL,
    education_level VARCHAR(50) NOT NULL,
    residential_status VARCHAR(50) NOT NULL,
    number_of_dependents INT NOT NULL,
    occupation VARCHAR(255) NOT NULL,
    employer_name VARCHAR(255) NOT NULL,
    monthly_income DOUBLE PRECISION NOT NULL,
    bank_name VARCHAR(255) NOT NULL,
    bank_account_number VARCHAR(50) NOT NULL,
    bank_sort_code VARCHAR(50) NOT NULL,
    next_of_kin_name VARCHAR(255) NOT NULL,
    next_of_kin_phone VARCHAR(50) NOT NULL,
    next_of_kin_relationship VARCHAR(50) NOT NULL,
    spouse_name VARCHAR(255),
    spouse_occupation VARCHAR(255),
    spouse_monthly_income DOUBLE PRECISION,
    preferred_language VARCHAR(50) NOT NULL,
    membership_channel VARCHAR(100) NOT NULL,
    referral_source VARCHAR(100) NOT NULL,
    active BOOLEAN NOT NULL
);

-- LOANS
CREATE TABLE loans (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT,
    deleted BOOLEAN NOT NULL,
    member_id UUID NOT NULL REFERENCES members(id),
    reference_number VARCHAR(100) UNIQUE NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL,
    term_in_months INT NOT NULL,
    monthly_payment NUMERIC(15,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    status VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL
);

-- DOCUMENTS
CREATE TABLE documents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT,
    deleted BOOLEAN NOT NULL,
    member_id UUID NOT NULL REFERENCES members(id),
    loan_id UUID REFERENCES loans(id),
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path TEXT NOT NULL,
    description TEXT,
    document_type VARCHAR(50) NOT NULL,
    version_number INT NOT NULL,
    signed BOOLEAN NOT NULL,
    signed_by VARCHAR(255),
    signed_at TIMESTAMP,
    signature_hash TEXT,
    active BOOLEAN NOT NULL,
    verified BOOLEAN NOT NULL
);

-- PAYMENTS
CREATE TABLE payments (
    id UUID PRIMARY KEY,
    created_at DATE NOT NULL,
    updated_at DATE NOT NULL,
    version INT NOT NULL,
    deleted BOOLEAN NOT NULL,
    loan_id UUID NOT NULL REFERENCES loans(id),
    amount NUMERIC(15,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    reference_number VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    notes TEXT,
    active BOOLEAN NOT NULL
);

-- INDEXES
CREATE INDEX idx_member_email ON members(email);
CREATE INDEX idx_member_phone ON members(phone_number);
CREATE INDEX idx_loan_reference_number ON loans(reference_number);
CREATE INDEX idx_payment_loan_id ON payments(loan_id);
CREATE INDEX idx_document_member_id ON documents(member_id);
CREATE INDEX idx_document_loan_id ON documents(loan_id);

-- SEED DATA
INSERT INTO members (id, created_at, updated_at, version, deleted, first_name, last_name, email, phone_number, date_of_birth, address, marital_status, education_level, residential_status, number_of_dependents, occupation, employer_name, monthly_income, bank_name, bank_account_number, bank_sort_code, next_of_kin_name, next_of_kin_phone, next_of_kin_relationship, spouse_name, spouse_occupation, spouse_monthly_income, preferred_language, membership_channel, referral_source, active)
VALUES (
    '00000000-0000-0000-0000-000000000001', NOW(), NOW(), 0, false,
    'John', 'Doe', 'john.doe@example.com', '555-1234', '1990-01-01', '123 Main St',
    'SINGLE', 'BACHELORS', 'OWNED', 0, 'Engineer', 'Tech Corp', 5000.00, 'Bank A', '12345678', '001122',
    'Jane Doe', '555-4321', 'Sister', NULL, NULL, NULL, 'English', 'Online', 'Referral', true
);

INSERT INTO loans (id, created_at, updated_at, version, deleted, member_id, reference_number, amount, interest_rate, term_in_months, monthly_payment, start_date, end_date, status, active)
VALUES (
    '00000000-0000-0000-0000-000000000010', NOW(), NOW(), 0, false,
    '00000000-0000-0000-0000-000000000001', 'REF123456', 10000.00, 5.5, 24, 440.00, '2024-01-01', '2025-12-31', 'ACTIVE', true
);

INSERT INTO documents (id, created_at, updated_at, version, deleted, member_id, loan_id, file_name, content_type, file_size, file_path, description, document_type, version_number, signed, signed_by, signed_at, signature_hash, active, verified)
VALUES (
    '00000000-0000-0000-0000-000000000100', NOW(), NOW(), 0, false,
    '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000010',
    'loan_agreement.pdf', 'application/pdf', 102400, '/docs/loan_agreement.pdf',
    'Initial loan agreement document', 'AGREEMENT', 1, false, NULL, NULL, NULL, true, false
);

INSERT INTO payments (id, created_at, updated_at, version, deleted, loan_id, amount, payment_date, payment_method, reference_number, status, notes, active)
VALUES (
    '00000000-0000-0000-0000-000000000200', CURRENT_DATE, CURRENT_DATE, 0, false,
    '00000000-0000-0000-0000-000000000010', 440.00, '2024-02-01', 'BANK_TRANSFER', 'TXN123456', 'COMPLETED', 'First payment', true
);
