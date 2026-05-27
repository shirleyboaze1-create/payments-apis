CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) DEFAULT 'MZN',
    phone_number VARCHAR(20) NOT NULL,
    reference VARCHAR(50) UNIQUE NOT NULL,
    transaction_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);