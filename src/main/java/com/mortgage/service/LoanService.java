package com.mortgage.service;

import com.mortgage.model.LoanApplication;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanProduct;
import com.mortgage.dao.LoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

/**
 * 住宅ローン業務サービス
 */
@Service
@Transactional
public class LoanService {

    @Autowired
    private LoanDao loanDao;

    /**
     * 全てのローン商品を取得
     */
    public List<LoanProduct> getAllLoanProducts() {
        return loanDao.getAllLoanProducts();
    }

    /**
     * 商品IDでローン商品を取得
     */
    public LoanProduct getLoanProductById(String productId) {
        return loanDao.getLoanProductById(productId);
    }

    /**
     * 見積もり計算
     */
    public LoanEstimate calculateEstimate(String productId, BigDecimal loanAmount, Integer loanTerm) {
        // 商品情報を取得
        LoanProduct product = loanDao.getLoanProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("指定された商品が見つかりません: " + productId);
        }

        // 入力値の検証
        validateLoanParameters(product, loanAmount, loanTerm);

        // 金利を取得（商品の現在金利を使用）
        BigDecimal interestRate = product.getCurrentInterestRate();

        // 月次返済額を計算
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, interestRate, loanTerm);

        // 総返済額と総利息を計算
        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(loanTerm * 12));
        BigDecimal totalInterest = totalPayment.subtract(loanAmount);

        // 見積もりオブジェクトを作成
        LoanEstimate estimate = new LoanEstimate(productId, product.getProductName(), 
                                               loanAmount, loanTerm, interestRate, monthlyPayment);
        estimate.setTotalPayment(totalPayment);
        estimate.setTotalInterest(totalInterest);
        estimate.setRepaymentMethod(product.getRepaymentMethod());

        // 見積もりを保存
        estimate.setEstimateId(generateEstimateId());
        loanDao.saveEstimate(estimate);

        return estimate;
    }

    /**
     * 申し込み処理
     */
    public LoanApplication submitApplication(LoanApplication application) {
        // 申し込みIDを生成
        application.setApplicationId(generateApplicationId());
        application.setCreatedAt(java.time.LocalDateTime.now());
        application.setUpdatedAt(java.time.LocalDateTime.now());

        // 申し込み情報を保存
        loanDao.saveApplication(application);

        // 自動審査処理
        performAutoReview(application);

        return application;
    }

    /**
     * 申し込み状況を取得
     */
    public LoanApplication getApplicationById(String applicationId) {
        return loanDao.getApplicationById(applicationId);
    }

    /**
     * 顧客の申し込み履歴を取得
     */
    public List<LoanApplication> getApplicationsByCustomerId(String customerId) {
        return loanDao.getApplicationsByCustomerId(customerId);
    }

    /**
     * 月次返済額を計算
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, Integer years) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            // 金利が0%の場合
            return principal.divide(BigDecimal.valueOf(years * 12), 2, RoundingMode.HALF_UP);
        }

        // 月次金利を計算
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        
        // 返済回数
        int numberOfPayments = years * 12;

        // 月次返済額の計算式: P * (r * (1 + r)^n) / ((1 + r)^n - 1)
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRateToN = onePlusRate.pow(numberOfPayments);
        
        BigDecimal numerator = monthlyRate.multiply(onePlusRateToN);
        BigDecimal denominator = onePlusRateToN.subtract(BigDecimal.ONE);
        
        return principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    }

    /**
     * ローン条件の検証
     */
    private void validateLoanParameters(LoanProduct product, BigDecimal loanAmount, Integer loanTerm) {
        // 融資額の検証
        if (loanAmount.compareTo(BigDecimal.valueOf(product.getMinLoanAmount())) < 0) {
            throw new IllegalArgumentException("融資額が最小額を下回っています: " + product.getMinLoanAmount());
        }
        if (loanAmount.compareTo(BigDecimal.valueOf(product.getMaxLoanAmount())) > 0) {
            throw new IllegalArgumentException("融資額が最大額を超えています: " + product.getMaxLoanAmount());
        }

        // 融資期間の検証
        if (loanTerm < product.getMinLoanTerm()) {
            throw new IllegalArgumentException("融資期間が最小期間を下回っています: " + product.getMinLoanTerm());
        }
        if (loanTerm > product.getMaxLoanTerm()) {
            throw new IllegalArgumentException("融資期間が最大期間を超えています: " + product.getMaxLoanTerm());
        }
    }

    /**
     * 自動審査処理
     */
    private void performAutoReview(LoanApplication application) {
        // 基本的な審査ロジック
        boolean isApproved = true;
        String rejectionReason = null;

        // 年収の4倍以内かチェック
        if (application.getAnnualIncome() != null && application.getLoanAmount() != null) {
            BigDecimal maxLoanAmount = application.getAnnualIncome().multiply(BigDecimal.valueOf(4));
            if (application.getLoanAmount().compareTo(maxLoanAmount) > 0) {
                isApproved = false;
                rejectionReason = "年収の4倍を超える融資額です";
            }
        }

        // 物件価格の80%以内かチェック
        if (application.getPropertyValue() != null && application.getLoanAmount() != null) {
            BigDecimal maxLoanAmount = application.getPropertyValue().multiply(BigDecimal.valueOf(0.8));
            if (application.getLoanAmount().compareTo(maxLoanAmount) > 0) {
                isApproved = false;
                rejectionReason = "物件価格の80%を超える融資額です";
            }
        }

        // 審査結果を更新
        if (isApproved) {
            application.setApplicationStatus("APPROVED");
            application.setApprovalDate(java.time.LocalDateTime.now());
            application.setApprovedBy("SYSTEM");
        } else {
            application.setApplicationStatus("REJECTED");
            application.setRejectionReason(rejectionReason);
        }

        loanDao.updateApplication(application);
    }

    /**
     * 見積もりIDを生成
     */
    private String generateEstimateId() {
        return "EST" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 申し込みIDを生成
     */
    private String generateApplicationId() {
        return "APP" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }
} 