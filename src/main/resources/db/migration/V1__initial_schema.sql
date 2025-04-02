-- Create Member table
CREATE TABLE member (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    nationality VARCHAR(100),
    marital_status VARCHAR(20),
    employment_status VARCHAR(20),
    monthly_income DECIMAL(12,2),
    number_of_dependents INTEGER,
    employer_name VARCHAR(255),
    employer_address VARCHAR(255),
    employer_phone VARCHAR(20),
    bank_name VARCHAR(255),
    bank_account_number VARCHAR(50),
    bank_branch VARCHAR(255),
    bank_swift_code VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create Loan table
CREATE TABLE loan (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    member_id UUID NOT NULL REFERENCES member(id),
    reference_number VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(12,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    term_months INTEGER NOT NULL,
    monthly_payment DECIMAL(12,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create Document table
CREATE TABLE document (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    member_id UUID NOT NULL REFERENCES member(id),
    loan_id UUID REFERENCES loan(id),
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path VARCHAR(1000) NOT NULL,
    description TEXT,
    document_type VARCHAR(50) NOT NULL,
    version_number INTEGER NOT NULL,
    signed BOOLEAN NOT NULL DEFAULT FALSE,
    signed_by VARCHAR(255),
    signed_at TIMESTAMP,
    signature_hash VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Create Payment table
CREATE TABLE payment (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version INTEGER NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    loan_id UUID NOT NULL REFERENCES loan(id),
    amount DECIMAL(12,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    reference_number VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
); 