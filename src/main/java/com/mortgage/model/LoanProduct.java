package com.mortgage.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 住宅ローン商品モデル
 */
public class LoanProduct {
    
    private String productId;
    private String productName;
    private String productType; // FIXED, VARIABLE, MIXED
    private BigDecimal minInterestRate;
    private BigDecimal maxInterestRate;
    private BigDecimal currentInterestRate;
    private Integer minLoanAmount;
    private Integer maxLoanAmount;
    private Integer minLoanTerm;
    private Integer maxLoanTerm;
    private String repaymentMethod; // EQUAL_PAYMENT, EQUAL_PRINCIPAL
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // コンストラクタ
    public LoanProduct() {}

    public LoanProduct(String productId, String productName, String productType, 
                      BigDecimal currentInterestRate, Integer minLoanAmount, 
                      Integer maxLoanAmount, Integer minLoanTerm, Integer maxLoanTerm) {
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.currentInterestRate = currentInterestRate;
        this.minLoanAmount = minLoanAmount;
        this.maxLoanAmount = maxLoanAmount;
        this.minLoanTerm = minLoanTerm;
        this.maxLoanTerm = maxLoanTerm;
    }

    // Getter and Setter methods
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getMinInterestRate() {
        return minInterestRate;
    }

    public void setMinInterestRate(BigDecimal minInterestRate) {
        this.minInterestRate = minInterestRate;
    }

    public BigDecimal getMaxInterestRate() {
        return maxInterestRate;
    }

    public void setMaxInterestRate(BigDecimal maxInterestRate) {
        this.maxInterestRate = maxInterestRate;
    }

    public BigDecimal getCurrentInterestRate() {
        return currentInterestRate;
    }

    public void setCurrentInterestRate(BigDecimal currentInterestRate) {
        this.currentInterestRate = currentInterestRate;
    }

    public Integer getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(Integer minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public Integer getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(Integer maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public Integer getMinLoanTerm() {
        return minLoanTerm;
    }

    public void setMinLoanTerm(Integer minLoanTerm) {
        this.minLoanTerm = minLoanTerm;
    }

    public Integer getMaxLoanTerm() {
        return maxLoanTerm;
    }

    public void setMaxLoanTerm(Integer maxLoanTerm) {
        this.maxLoanTerm = maxLoanTerm;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "LoanProduct{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", currentInterestRate=" + currentInterestRate +
                ", minLoanAmount=" + minLoanAmount +
                ", maxLoanAmount=" + maxLoanAmount +
                ", minLoanTerm=" + minLoanTerm +
                ", maxLoanTerm=" + maxLoanTerm +
                ", isActive=" + isActive +
                '}';
    }
} 