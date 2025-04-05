-- Create documents table
CREATE TABLE IF NOT EXISTS documents (
                                         id UUID PRIMARY KEY,
                                         display_id VARCHAR(100) NOT NULL UNIQUE,
    member_id UUID NOT NULL REFERENCES members(id),
    loan_id UUID REFERENCES loans(id),
    file_name VARCHAR(255) NOT NULL,
    deleted BOOLEAN NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    file_path TEXT NOT NULL,
    description TEXT,
    document_type VARCHAR(50) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0, -- renamed from version_number to version
    signed BOOLEAN DEFAULT FALSE,
    signed_by VARCHAR(255),
    signed_at TIMESTAMP,
    signature_hash TEXT,
    active BOOLEAN DEFAULT TRUE,
    verified BOOLEAN DEFAULT FALSE,
    uploaded_at TIMESTAMP DEFAULT now(),

    -- BaseEntity auditing fields
    created_at TIMESTAMP DEFAULT now() NOT NULL,
    updated_at TIMESTAMP DEFAULT now() NOT NULL,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    last_modified_by VARCHAR(255) NOT NULL DEFAULT 'system'
    );

-- Indexes
CREATE INDEX IF NOT EXISTS idx_documents_member_id ON documents(member_id);
CREATE INDEX IF NOT EXISTS idx_documents_loan_id ON documents(loan_id);
CREATE INDEX IF NOT EXISTS idx_documents_document_type ON documents(document_type);

-- Insert mock data
INSERT INTO documents (
    id, display_id, member_id, loan_id, file_name, deleted, content_type, file_size,
    file_path, description, document_type, version,
    signed, signed_by, signed_at, signature_hash,
    active, verified, uploaded_at, created_by, updated_at, last_modified_by
)
VALUES
    (
        gen_random_uuid(), 'DOC-0001',
        (SELECT id FROM members LIMIT 1),
    (SELECT id FROM loans LIMIT 1),
    'ID_Passport.pdf', false, 'application/pdf', 204800,
    '/docs/ID_Passport.pdf', 'Passport document',
    'ID_PROOF', 0,
    false, null, null, null,
    true, false, now(), 'system', now(), 'system'
    ),
(
    gen_random_uuid(), 'DOC-0002',
    (SELECT id FROM members OFFSET 1 LIMIT 1),
    null,
    'Loan_Agreement.pdf', false, 'application/pdf', 350000,
    '/docs/Loan_Agreement.pdf', 'Agreement signed digitally',
    'ID_PROOF', 0,
    true, 'corey.m', now(), 'abc123hash',
    true, true, now(), 'admin', now(), 'admin'
);
