-- 住宅ローンシステム データベース初期化SQL

-- データベース作成
CREATE DATABASE IF NOT EXISTS mortgage_loan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mortgage_loan;

-- ローン商品テーブル
CREATE TABLE loan_products (
    product_id VARCHAR(50) PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    product_type VARCHAR(20) NOT NULL, -- FIXED, VARIABLE, MIXED
    min_interest_rate DECIMAL(5,2),
    max_interest_rate DECIMAL(5,2),
    current_interest_rate DECIMAL(5,2) NOT NULL,
    min_loan_amount INT NOT NULL,
    max_loan_amount INT NOT NULL,
    min_loan_term INT NOT NULL,
    max_loan_term INT NOT NULL,
    repayment_method VARCHAR(20) NOT NULL, -- EQUAL_PAYMENT, EQUAL_PRINCIPAL
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 見積もりテーブル
CREATE TABLE loan_estimates (
    estimate_id VARCHAR(100) PRIMARY KEY,
    product_id VARCHAR(50) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    loan_amount DECIMAL(15,2) NOT NULL,
    loan_term INT NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    monthly_payment DECIMAL(15,2) NOT NULL,
    total_payment DECIMAL(15,2),
    total_interest DECIMAL(15,2),
    repayment_method VARCHAR(20),
    estimated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    customer_id VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES loan_products(product_id)
);

-- 申し込みテーブル
CREATE TABLE loan_applications (
    application_id VARCHAR(100) PRIMARY KEY,
    customer_id VARCHAR(50),
    customer_name VARCHAR(100) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    customer_phone VARCHAR(20),
    product_id VARCHAR(50) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    loan_amount DECIMAL(15,2) NOT NULL,
    loan_term INT NOT NULL,
    interest_rate DECIMAL(5,2),
    monthly_payment DECIMAL(15,2),
    application_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED, PROCESSING
    property_address TEXT,
    property_type VARCHAR(20), -- DETACHED, APARTMENT, CONDO
    property_value DECIMAL(15,2),
    employment_type VARCHAR(20), -- SALARIED, SELF_EMPLOYED, BUSINESS_OWNER
    annual_income DECIMAL(15,2),
    bank_account_number VARCHAR(20),
    bank_name VARCHAR(100),
    branch_name VARCHAR(100),
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP NULL,
    approved_by VARCHAR(50),
    rejection_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES loan_products(product_id)
);

-- サンプルデータ: ローン商品
INSERT INTO loan_products (product_id, product_name, product_type, min_interest_rate, max_interest_rate, current_interest_rate, min_loan_amount, max_loan_amount, min_loan_term, max_loan_term, repayment_method, description) VALUES
('FIXED_001', '固定金利プランA', 'FIXED', 0.50, 1.20, 0.85, 100, 5000, 10, 35, 'EQUAL_PAYMENT', '安定した返済額で安心の固定金利プランです。'),
('FIXED_002', '固定金利プランB', 'FIXED', 0.60, 1.30, 0.95, 200, 8000, 15, 35, 'EQUAL_PAYMENT', '長期固定で金利上昇リスクを回避できます。'),
('VARIABLE_001', '変動金利プランA', 'VARIABLE', 0.30, 1.50, 0.45, 100, 10000, 10, 35, 'EQUAL_PAYMENT', '低金利で始められる変動金利プランです。'),
('VARIABLE_002', '変動金利プランB', 'VARIABLE', 0.35, 1.60, 0.50, 200, 12000, 15, 35, 'EQUAL_PAYMENT', '金利優遇のある変動金利プランです。'),
('MIXED_001', 'ミックスプランA', 'MIXED', 0.40, 1.40, 0.70, 150, 7000, 10, 30, 'EQUAL_PAYMENT', '固定金利と変動金利を組み合わせたプランです。'),
('MIXED_002', 'ミックスプランB', 'MIXED', 0.45, 1.45, 0.75, 300, 9000, 15, 30, 'EQUAL_PAYMENT', 'リスク分散を重視したミックスプランです。');

-- インデックス作成
CREATE INDEX idx_loan_products_active ON loan_products(is_active);
CREATE INDEX idx_loan_estimates_customer ON loan_estimates(customer_id);
CREATE INDEX idx_loan_applications_customer ON loan_applications(customer_id);
CREATE INDEX idx_loan_applications_status ON loan_applications(application_status);
CREATE INDEX idx_loan_applications_date ON loan_applications(application_date); 