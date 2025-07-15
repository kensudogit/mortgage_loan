package com.mortgage.dao;

import com.mortgage.model.LoanApplication;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanProduct;

import java.util.List;

/**
 * 住宅ローンDAOインターフェース
 */
public interface LoanDao {

    /**
     * 全てのローン商品を取得
     */
    List<LoanProduct> getAllLoanProducts();

    /**
     * 商品IDでローン商品を取得
     */
    LoanProduct getLoanProductById(String productId);

    /**
     * 見積もりを保存
     */
    void saveEstimate(LoanEstimate estimate);

    /**
     * 見積もりIDで見積もりを取得
     */
    LoanEstimate getEstimateById(String estimateId);

    /**
     * 申し込みを保存
     */
    void saveApplication(LoanApplication application);

    /**
     * 申し込みを更新
     */
    void updateApplication(LoanApplication application);

    /**
     * 申し込みIDで申し込みを取得
     */
    LoanApplication getApplicationById(String applicationId);

    /**
     * 顧客IDで申し込み履歴を取得
     */
    List<LoanApplication> getApplicationsByCustomerId(String customerId);

    /**
     * 申し込み状況で申し込み一覧を取得
     */
    List<LoanApplication> getApplicationsByStatus(String status);
} 