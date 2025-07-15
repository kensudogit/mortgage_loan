package com.mortgage.integration;

import com.mortgage.model.LoanProduct;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanApplication;
import com.mortgage.service.LoanService;
import com.mortgage.dao.LoanDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 住宅ローンシステムの統合テスト
 * 実際のデータベースを使用して、サービス層とDAO層の連携をテスト
 * IDEで▷ボタンをクリックして個別のテストメソッドを実行できます
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanIntegrationTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanDao loanDao;

    private LoanProduct testProduct;
    private LoanApplication testApplication;

    @Before
    public void setUp() {
        // テスト用のローン商品データ
        testProduct = new LoanProduct();
        testProduct.setProductId("INTEGRATION_TEST_001");
        testProduct.setProductName("統合テスト用住宅ローン");
        testProduct.setProductType("FIXED");
        testProduct.setCurrentInterestRate(1.5);
        testProduct.setMinLoanAmount(1000);
        testProduct.setMaxLoanAmount(5000);
        testProduct.setMinLoanTerm(10);
        testProduct.setMaxLoanTerm(35);
        testProduct.setRepaymentMethod("EQUAL_PAYMENT");
        testProduct.setIsActive(true);

        // テスト用の申し込みデータ
        testApplication = new LoanApplication();
        testApplication.setCustomerName("統合テスト太郎");
        testApplication.setCustomerEmail("integration@test.com");
        testApplication.setCustomerPhone("090-9999-9999");
        testApplication.setProductId("INTEGRATION_TEST_001");
        testApplication.setProductName("統合テスト用住宅ローン");
        testApplication.setLoanAmount(3000);
        testApplication.setLoanTerm(30);
        testApplication.setInterestRate(1.5);
        testApplication.setMonthlyPayment(10350);
        testApplication.setPropertyAddress("東京都新宿区統合テスト1-1-1");
        testApplication.setPropertyType("DETACHED");
        testApplication.setPropertyValue(4000);
        testApplication.setEmploymentType("SALARIED");
        testApplication.setAnnualIncome(600);
        testApplication.setBankAccountNumber("9999999999");
        testApplication.setBankName("統合テスト銀行");
        testApplication.setBranchName("新宿支店");
        testApplication.setApplicationStatus("PENDING");
    }

    /**
     * 全商品取得の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_Integration() {
        // When
        List<LoanProduct> products = loanService.getAllProducts();

        // Then
        assertNotNull(products);
        assertFalse(products.isEmpty());
        
        // 商品の基本情報を検証
        for (LoanProduct product : products) {
            assertNotNull(product.getProductId());
            assertNotNull(product.getProductName());
            assertTrue(product.getCurrentInterestRate() > 0);
            assertTrue(product.getMinLoanAmount() > 0);
            assertTrue(product.getMaxLoanAmount() > product.getMinLoanAmount());
            assertTrue(product.getMinLoanTerm() > 0);
            assertTrue(product.getMaxLoanTerm() > product.getMinLoanTerm());
        }
    }

    /**
     * 商品IDによる商品取得の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProductById_Integration() {
        // Given
        String productId = "PROD001"; // 初期データから

        // When
        LoanProduct product = loanService.getProductById(productId);

        // Then
        assertNotNull(product);
        assertEquals(productId, product.getProductId());
        assertEquals("固定金利住宅ローン", product.getProductName());
        assertEquals("FIXED", product.getProductType());
        assertEquals(1.5, product.getCurrentInterestRate(), 0.01);
    }

    /**
     * 見積もり計算の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_Integration() {
        // Given
        String productId = "PROD001";
        int loanAmount = 3000;
        int loanTerm = 30;

        // When
        LoanEstimate estimate = loanService.calculateEstimate(productId, loanAmount, loanTerm);

        // Then
        assertNotNull(estimate);
        assertEquals(productId, estimate.getProductId());
        assertEquals(loanAmount, estimate.getLoanAmount());
        assertEquals(loanTerm, estimate.getLoanTerm());
        assertEquals(1.5, estimate.getInterestRate(), 0.01);
        assertTrue(estimate.getMonthlyPayment() > 0);
        assertTrue(estimate.getTotalPayment() > loanAmount * 10000); // 万円単位なので10000倍
        assertTrue(estimate.getTotalInterest() > 0);
    }

    /**
     * 申し込み提出の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_Integration() {
        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        assertNotNull(result.getApplicationId());
        assertTrue(result.getApplicationId().startsWith("APP"));
        assertEquals("PENDING", result.getApplicationStatus());
        assertEquals("統合テスト太郎", result.getCustomerName());
        assertEquals("integration@test.com", result.getCustomerEmail());
        assertEquals(3000, result.getLoanAmount());
        assertEquals(30, result.getLoanTerm());
        assertEquals(1.5, result.getInterestRate(), 0.01);
        assertEquals(10350, result.getMonthlyPayment());
    }

    /**
     * 申し込み提出の自動承認統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_AutoApproval_Integration() {
        // Given - 高収入・高額物件で自動承認条件を満たす
        testApplication.setAnnualIncome(800);
        testApplication.setPropertyValue(5000);

        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        assertEquals("APPROVED", result.getApplicationStatus());
        assertNotNull(result.getApprovedBy());
        assertNotNull(result.getApprovalDate());
    }

    /**
     * 申し込み提出の自動却下統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_AutoRejection_Integration() {
        // Given - 低収入・低額物件で自動却下条件を満たす
        testApplication.setAnnualIncome(300);
        testApplication.setPropertyValue(2000);

        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        assertEquals("REJECTED", result.getApplicationStatus());
        assertNotNull(result.getRejectionReason());
    }

    /**
     * 申し込みIDによる申し込み取得の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationById_Integration() {
        // Given
        LoanApplication savedApplication = loanService.submitApplication(testApplication);
        String applicationId = savedApplication.getApplicationId();

        // When
        LoanApplication retrievedApplication = loanService.getApplicationById(applicationId);

        // Then
        assertNotNull(retrievedApplication);
        assertEquals(applicationId, retrievedApplication.getApplicationId());
        assertEquals("統合テスト太郎", retrievedApplication.getCustomerName());
        assertEquals("integration@test.com", retrievedApplication.getCustomerEmail());
        assertEquals(3000, retrievedApplication.getLoanAmount());
        assertEquals(30, retrievedApplication.getLoanTerm());
    }

    /**
     * 顧客IDによる申し込み一覧取得の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationsByCustomerId_Integration() {
        // Given
        LoanApplication savedApplication = loanService.submitApplication(testApplication);
        String customerId = savedApplication.getCustomerId();

        // When
        List<LoanApplication> applications = loanService.getApplicationsByCustomerId(customerId);

        // Then
        assertNotNull(applications);
        assertFalse(applications.isEmpty());
        assertTrue(applications.stream().anyMatch(app -> 
            app.getApplicationId().equals(savedApplication.getApplicationId())));
    }

    /**
     * 見積もり計算の等額返済統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_EqualPaymentMethod_Integration() {
        // Given
        String productId = "PROD001";
        int loanAmount = 3000;
        int loanTerm = 30;

        // When
        LoanEstimate estimate = loanService.calculateEstimate(productId, loanAmount, loanTerm);

        // Then
        assertNotNull(estimate);
        assertEquals("EQUAL_PAYMENT", estimate.getRepaymentMethod());
        
        // 等額返済の場合の計算結果を検証
        double monthlyRate = estimate.getInterestRate() / 100 / 12;
        int totalMonths = estimate.getLoanTerm() * 12;
        double expectedMonthlyPayment = (estimate.getLoanAmount() * 10000 * monthlyRate * 
            Math.pow(1 + monthlyRate, totalMonths)) / 
            (Math.pow(1 + monthlyRate, totalMonths) - 1);
        
        assertEquals(expectedMonthlyPayment, estimate.getMonthlyPayment(), 100); // 誤差100円以内
    }

    /**
     * 見積もり計算の元金均等返済統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_EqualPrincipalMethod_Integration() {
        // Given - 元金均等返済の商品を使用
        String productId = "PROD002"; // 変動金利商品（元金均等返済）
        int loanAmount = 3000;
        int loanTerm = 30;

        // When
        LoanEstimate estimate = loanService.calculateEstimate(productId, loanAmount, loanTerm);

        // Then
        assertNotNull(estimate);
        assertEquals("EQUAL_PRINCIPAL", estimate.getRepaymentMethod());
        
        // 元金均等返済の場合の計算結果を検証
        double monthlyPrincipal = (estimate.getLoanAmount() * 10000) / (estimate.getLoanTerm() * 12);
        double firstMonthInterest = (estimate.getLoanAmount() * 10000) * (estimate.getInterestRate() / 100 / 12);
        double expectedFirstMonthPayment = monthlyPrincipal + firstMonthInterest;
        
        assertEquals(expectedFirstMonthPayment, estimate.getMonthlyPayment(), 100); // 誤差100円以内
    }

    /**
     * 無効な融資額の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testInvalidLoanAmount_Integration() {
        // Given
        String productId = "PROD001";
        int invalidLoanAmount = 500; // 最小額未満
        int loanTerm = 30;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate(productId, invalidLoanAmount, loanTerm);
        });
    }

    /**
     * 無効な融資期間の統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testInvalidLoanTerm_Integration() {
        // Given
        String productId = "PROD001";
        int loanAmount = 3000;
        int invalidLoanTerm = 5; // 最小期間未満

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate(productId, loanAmount, invalidLoanTerm);
        });
    }

    /**
     * 無効な商品IDの統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testInvalidProductId_Integration() {
        // Given
        String invalidProductId = "INVALID_PRODUCT";
        int loanAmount = 3000;
        int loanTerm = 30;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate(invalidProductId, loanAmount, loanTerm);
        });
    }

    /**
     * 申し込みバリデーションの統合テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testApplicationValidation_Integration() {
        // Given - 無効な申し込みデータ
        testApplication.setCustomerName(""); // 空の名前
        testApplication.setCustomerEmail("invalid-email"); // 無効なメール

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.submitApplication(testApplication);
        });
    }
} 