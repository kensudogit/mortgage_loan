package com.mortgage.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 住宅ローン見積もりモデル
 */
public class LoanEstimate {
    
    private String estimateId;
    private String productId;
    private String productName;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private BigDecimal interestRate;
    private BigDecimal monthlyPayment;
    private BigDecimal totalPayment;
    private BigDecimal totalInterest;
    private String repaymentMethod;
    private LocalDateTime estimatedAt;
    private String customerId;

    // コンストラクタ
    public LoanEstimate() {}

    public LoanEstimate(String productId, String productName, BigDecimal loanAmount, 
                       Integer loanTerm, BigDecimal interestRate, BigDecimal monthlyPayment) {
        this.productId = productId;
        this.productName = productName;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.interestRate = interestRate;
        this.monthlyPayment = monthlyPayment;
        this.estimatedAt = LocalDateTime.now();
    }

    // Getter and Setter methods
    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

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

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public LocalDateTime getEstimatedAt() {
        return estimatedAt;
    }

    public void setEstimatedAt(LocalDateTime estimatedAt) {
        this.estimatedAt = estimatedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "LoanEstimate{" +
                "estimateId='" + estimateId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanTerm=" + loanTerm +
                ", interestRate=" + interestRate +
                ", monthlyPayment=" + monthlyPayment +
                ", totalPayment=" + totalPayment +
                ", totalInterest=" + totalInterest +
                ", estimatedAt=" + estimatedAt +
                '}';
    }
} 