package com.mortgage.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 住宅ローン申し込みモデル
 */
public class LoanApplication {
    
    private String applicationId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String productId;
    private String productName;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private BigDecimal interestRate;
    private BigDecimal monthlyPayment;
    private String applicationStatus; // PENDING, APPROVED, REJECTED, PROCESSING
    private String propertyAddress;
    private String propertyType; // DETACHED, APARTMENT, CONDO
    private BigDecimal propertyValue;
    private String employmentType; // SALARIED, SELF_EMPLOYED, BUSINESS_OWNER
    private BigDecimal annualIncome;
    private String bankAccountNumber;
    private String bankName;
    private String branchName;
    private LocalDateTime applicationDate;
    private LocalDateTime approvalDate;
    private String approvedBy;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // コンストラクタ
    public LoanApplication() {}

    public LoanApplication(String customerId, String customerName, String productId, 
                         BigDecimal loanAmount, Integer loanTerm) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.applicationStatus = "PENDING";
        this.applicationDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Getter and Setter methods
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public BigDecimal getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(BigDecimal propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
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
        return "LoanApplication{" +
                "applicationId='" + applicationId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", productId='" + productId + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanTerm=" + loanTerm +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", applicationDate=" + applicationDate +
                '}';
    }
} 