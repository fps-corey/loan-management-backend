
-- V1__init_schema.sql (combined and with indexes)

-- MEMBERS
CREATE TABLE members (
                         id UUID PRIMARY KEY,
                         display_id VARCHAR(20) NOT NULL,
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
                       display_id VARCHAR(20) NOT NULL,
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
                       existing_amount NUMERIC(15,2),
                       fees NUMERIC(15,2),
                       total_loan_amount NUMERIC(15,2),
                       issue_account VARCHAR(100),
                       num_payments INT,
                       loan_type VARCHAR(100),
                       loan_purpose TEXT,
                       marketing_campaign TEXT,
                       additional_services TEXT,
                       active BOOLEAN NOT NULL
);
-- DOCUMENTS
CREATE TABLE documents (
                           id UUID PRIMARY KEY,
                           display_id VARCHAR(20) NOT NULL,
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
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          version BIGINT,
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

CREATE TABLE loan_approvals (
                                id UUID PRIMARY KEY,
                                created_at TIMESTAMP NOT NULL,
                                updated_at TIMESTAMP NOT NULL,
                                version BIGINT,
                                deleted BOOLEAN NOT NULL,
                                loan_id UUID NOT NULL REFERENCES loans(id) ON DELETE CASCADE,
                                approver VARCHAR(255) NOT NULL,
                                date DATE NOT NULL,
                                decision VARCHAR(100) NOT NULL,
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
CREATE INDEX idx_loan_approvals_loan_id ON loan_approvals(loan_id);

-- SEED DATA
INSERT INTO members (
    id, display_id, created_at, updated_at, version, deleted,
    first_name, last_name, email, phone_number, date_of_birth,
    address, marital_status, education_level, residential_status,
    number_of_dependents, occupation, employer_name, monthly_income,
    bank_name, bank_account_number, bank_sort_code,
    next_of_kin_name, next_of_kin_phone, next_of_kin_relationship,
    spouse_name, spouse_occupation, spouse_monthly_income,
    preferred_language, membership_channel, referral_source, active
)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'MEMBER-0001', NOW(), NOW(), 0, false,
     'John', 'Doe', 'john.doe@example.com', '555-1234', '1990-01-01',
     '123 Main St', 'SINGLE', 'BACHELORS', 'OWNED',
     0, 'Engineer', 'Tech Corp', 5000.00,
     'Bank A', '12345678', '001122',
     'Jane Doe', '555-4321', 'Sister',
     NULL, NULL, NULL,
     'English', 'Online', 'Referral', true),

    ('00000000-0000-0000-0000-000000000002', 'MEMBER-0002', NOW(), NOW(), 0, false,
     'Alice', 'Smith', 'alice.smith@example.com', '555-1002', '1985-04-12',
     '456 Oak Ave', 'MARRIED', 'MASTERS', 'RENTED',
     2, 'Analyst', 'FinanceCorp', 6000.00,
     'Bank B', '22223333', '004455',
     'Bob Smith', '555-5678', 'Husband',
     NULL, NULL, NULL,
     'English', 'Branch', 'Online Ad', true),

    ('00000000-0000-0000-0000-000000000003', 'MEMBER-0003', NOW(), NOW(), 0, false,
     'Carlos', 'Nguyen', 'carlos.nguyen@example.com', '555-1003', '1992-07-19',
     '789 Pine Blvd', 'SINGLE', 'SECONDARY', 'OWNED',
     0, 'Teacher', 'High School', 4200.00,
     'Bank C', '33334444', '007788',
     'Lan Nguyen', '555-8765', 'Mother',
     NULL, NULL, NULL,
     'English', 'Mobile', 'Friend', true),

    ('00000000-0000-0000-0000-000000000004', 'MEMBER-0004', NOW(), NOW(), 0, false,
     'Fatima', 'Hassan', 'fatima.hassan@example.com', '555-1004', '1988-11-03',
     '321 Elm St', 'MARRIED', 'MASTERS', 'OWNED',
     1, 'Researcher', 'University', 7500.00,
     'Bank D', '44445555', '002211',
     'Ali Hassan', '555-3333', 'Husband',
     NULL, NULL, NULL,
     'Arabic', 'Online', 'Conference', true),

    ('00000000-0000-0000-0000-000000000005', 'MEMBER-0005', NOW(), NOW(), 0, false,
     'George', 'Okoro', 'george.okoro@example.com', '555-1005', '1990-02-28',
     '900 Birch Way', 'SINGLE', 'SECONDARY', 'RENTED',
     3, 'Driver', 'Logistics Ltd', 3000.00,
     'Bank E', '55556666', '003344',
     'Emeka Okoro', '555-2222', 'Brother',
     NULL, NULL, NULL,
     'English', 'Agent', 'Referral', true);

-- Loans with mixed member IDs
INSERT INTO loans (
    id, display_id, created_at, updated_at, version, deleted,
    member_id, reference_number,
    amount, interest_rate, term_in_months, monthly_payment,
    start_date, end_date, status, active,
    existing_amount, fees, total_loan_amount,
    issue_account, num_payments, loan_type, loan_purpose,
    marketing_campaign, additional_services
)
VALUES
    ('00000000-0000-0000-0000-000000000011', 'LOAN-0001', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000002', 'REFREQ001',
     5000.00, 6.0, 12, 445.00,
     '2024-01-01', '2024-12-31', 'REQUESTED', true,
     1000.00, 50.00, 5050.00,
     'AC001', 12, 'PERSONAL', 'Home Renovation',
     'Spring Promo', 'SMS Alerts'),

    ('00000000-0000-0000-0000-000000000012', 'LOAN-0002', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000003', 'REFDOC002',
     7000.00, 5.0, 18, 395.00,
     '2024-01-01', '2025-06-30', 'PENDING_DOCS', true,
     1500.00, 70.00, 7070.00,
     'AC002', 18, 'EDUCATION', 'Online Course',
     'Email Campaign', 'Loan Insurance'),

    ('00000000-0000-0000-0000-000000000013', 'LOAN-0003', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000005', 'REFINT003',
     8500.00, 4.8, 24, 385.00,
     '2024-02-01', '2026-01-31', 'PENDING_INTERVIEW', true,
     2000.00, 85.00, 8585.00,
     'AC003', 24, 'STARTUP', 'Initial Capital',
     'Social Media', 'Consulting'),

    ('00000000-0000-0000-0000-000000000014', 'LOAN-0004', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000004', 'REFCOM004',
     12000.00, 5.2, 36, 370.00,
     '2024-03-01', '2027-02-28', 'PENDING_COMMITTEE', true,
     3000.00, 100.00, 12100.00,
     'AC004', 36, 'BUSINESS', 'Equipment Purchase',
     'Radio Ads', 'Support Package'),

    ('00000000-0000-0000-0000-000000000015', 'LOAN-0005', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000001', 'REFAPR005',
     15000.00, 4.5, 30, 410.00,
     '2024-04-01', '2026-09-30', 'PENDING_APPROVAL', true,
     2500.00, 120.00, 15120.00,
     'AC005', 30, 'PERSONAL', 'Medical Bills',
     'TV Spot', 'Travel Discount'),

    ('00000000-0000-0000-0000-000000000016', 'LOAN-0006', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000002', 'REFPAY006',
     9500.00, 5.3, 20, 512.00,
     '2024-05-01', '2025-12-31', 'AWAITING_PAYOUT', true,
     1800.00, 65.00, 9565.00,
     'AC006', 20, 'MORTGAGE', 'Home Deposit',
     'Online Ads', 'Extended Warranty'),

    ('00000000-0000-0000-0000-000000000017', 'LOAN-0007', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000003', 'REFOUT007',
     8000.00, 4.7, 16, 515.00,
     '2024-06-01', '2025-09-30', 'PAYOUT_CONFIRMED', true,
     1600.00, 80.00, 8080.00,
     'AC007', 16, 'BUSINESS', 'Working Capital',
     'Billboard', 'Accounting Help'),

    ('00000000-0000-0000-0000-000000000018', 'LOAN-0008', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000005', 'REFARR008',
     11000.00, 5.9, 28, 430.00,
     '2024-07-01', '2026-10-31', 'IN_ARREARS', true,
     3500.00, 90.00, 11090.00,
     'AC008', 28, 'CAR', 'Vehicle Purchase',
     'Newspaper', 'Extended Warranty'),

    ('00000000-0000-0000-0000-000000000019', 'LOAN-0009', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000004', 'REFACT009',
     10000.00, 5.5, 24, 440.00,
     '2024-01-01', '2025-12-31', 'ACTIVE', true,
     2000.00, 75.00, 10075.00,
     'AC009', 24, 'EDUCATION', 'Masters Tuition',
     'Flyers', 'Study Tools'),

    ('00000000-0000-0000-0000-000000000020', 'LOAN-0010', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000001', 'REFCMP010',
     10000.00, 5.0, 24, 440.00,
     '2022-01-01', '2023-12-31', 'COMPLETE', false,
     1000.00, 60.00, 10060.00,
     'AC010', 24, 'PERSONAL', 'Debt Consolidation',
     'Event Booth', 'Counseling'),

    ('00000000-0000-0000-0000-000000000021', 'LOAN-0011', NOW(), NOW(), 0, false,
     '00000000-0000-0000-0000-000000000005', 'REFREJ011',
     6000.00, 6.5, 18, 390.00,
     '2024-08-01', '2026-01-31', 'REJECTED', false,
     500.00, 55.00, 6055.00,
     'AC011', 18, 'STARTUP', 'App Launch',
     'Digital Ads', 'None');







-- -- Document that references the loan above
-- INSERT INTO documents (id, created_at, updated_at, version, deleted, member_id, loan_id, file_name, content_type, file_size, file_path, description, document_type, version_number, signed, signed_by, signed_at, signature_hash, active, verified)
-- VALUES (
--            '00000000-0000-0000-0000-000000000100', NOW(), NOW(), 0, false,
--            '00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000010',
--            'loan_agreement.pdf', 'application/pdf', 102400, '/docs/loan_agreement.pdf',
--            'Initial loan agreement document', 'AGREEMENT', 1, false, NULL, NULL, NULL, true, false
--        );
--
-- INSERT INTO payments (id, created_at, updated_at, version, deleted, loan_id, amount, payment_date, payment_method, reference_number, status, notes, active)
-- VALUES (
--            '00000000-0000-0000-0000-000000000200', CURRENT_DATE, CURRENT_DATE, 0, false,
--            '00000000-0000-0000-0000-000000000010', 440.00, '2024-02-01', 'BANK_TRANSFER', 'TXN123456', 'COMPLETED', 'First payment', true
--        );

-- -- LOAN APPROVALS
-- INSERT INTO loan_approvals (
--     id, created_at, updated_at, version, deleted, loan_id,
--     approver, date, decision, notes, active
-- ) VALUES (
--              '00000000-0000-0000-0000-000000000300', NOW(), NOW(), 0, false,
--              '00000000-0000-0000-0000-000000000010',
--              'Credit Committee', '2024-01-05', 'APPROVED', 'Approved after credit check', true
--          );
