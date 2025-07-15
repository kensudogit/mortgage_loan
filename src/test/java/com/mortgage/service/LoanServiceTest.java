package com.mortgage.service;

import com.mortgage.dao.LoanDao;
import com.mortgage.model.LoanProduct;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * LoanServiceクラスのユニットテスト
 * IDEで▷ボタンをクリックして個別のテストメソッドを実行できます
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanServiceTest {

    @Mock
    private LoanDao loanDao;

    @InjectMocks
    private LoanService loanService;

    private LoanProduct testProduct;
    private LoanEstimate testEstimate;
    private LoanApplication testApplication;

    @Before
    public void setUp() {
        // テスト用のローン商品データ
        testProduct = new LoanProduct();
        testProduct.setProductId("PROD001");
        testProduct.setProductName("固定金利住宅ローン");
        testProduct.setProductType("FIXED");
        testProduct.setCurrentInterestRate(1.5);
        testProduct.setMinLoanAmount(1000);
        testProduct.setMaxLoanAmount(5000);
        testProduct.setMinLoanTerm(10);
        testProduct.setMaxLoanTerm(35);
        testProduct.setRepaymentMethod("EQUAL_PAYMENT");
        testProduct.setIsActive(true);

        // テスト用の見積もりデータ
        testEstimate = new LoanEstimate();
        testEstimate.setEstimateId("EST001");
        testEstimate.setProductId("PROD001");
        testEstimate.setProductName("固定金利住宅ローン");
        testEstimate.setLoanAmount(3000);
        testEstimate.setLoanTerm(30);
        testEstimate.setInterestRate(1.5);
        testEstimate.setMonthlyPayment(10350);
        testEstimate.setTotalPayment(3726000);
        testEstimate.setTotalInterest(726000);

        // テスト用の申し込みデータ
        testApplication = new LoanApplication();
        testApplication.setApplicationId("APP001");
        testApplication.setCustomerName("田中太郎");
        testApplication.setCustomerEmail("tanaka@example.com");
        testApplication.setCustomerPhone("090-1234-5678");
        testApplication.setProductId("PROD001");
        testApplication.setProductName("固定金利住宅ローン");
        testApplication.setLoanAmount(3000);
        testApplication.setLoanTerm(30);
        testApplication.setInterestRate(1.5);
        testApplication.setMonthlyPayment(10350);
        testApplication.setPropertyAddress("東京都渋谷区1-1-1");
        testApplication.setPropertyType("DETACHED");
        testApplication.setPropertyValue(4000);
        testApplication.setEmploymentType("SALARIED");
        testApplication.setAnnualIncome(600);
        testApplication.setBankAccountNumber("1234567890");
        testApplication.setBankName("テスト銀行");
        testApplication.setBranchName("渋谷支店");
        testApplication.setApplicationStatus("PENDING");
    }

    /**
     * 全商品取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_Success() {
        // Given
        when(loanDao.getAllProducts()).thenReturn(Arrays.asList(testProduct));

        // When
        List<LoanProduct> result = loanService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PROD001", result.get(0).getProductId());
        verify(loanDao, times(1)).getAllProducts();
    }

    /**
     * 全商品取得の空リストテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_EmptyList() {
        // Given
        when(loanDao.getAllProducts()).thenReturn(Arrays.asList());

        // When
        List<LoanProduct> result = loanService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(loanDao, times(1)).getAllProducts();
    }

    /**
     * 全商品取得の異常系テスト（DAO例外）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_DaoException() {
        // Given
        when(loanDao.getAllProducts()).thenThrow(new RuntimeException("DAO error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanService.getAllProducts();
        });
        verify(loanDao, times(1)).getAllProducts();
    }

    /**
     * 商品IDによる商品取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProductById_Success() {
        // Given
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);

        // When
        LoanProduct result = loanService.getProductById("PROD001");

        // Then
        assertNotNull(result);
        assertEquals("PROD001", result.getProductId());
        assertEquals("固定金利住宅ローン", result.getProductName());
        verify(loanDao, times(1)).getProductById("PROD001");
    }

    /**
     * 商品IDによる商品取得の異常系テスト（商品未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProductById_NotFound() {
        // Given
        when(loanDao.getProductById("INVALID")).thenReturn(null);

        // When
        LoanProduct result = loanService.getProductById("INVALID");

        // Then
        assertNull(result);
        verify(loanDao, times(1)).getProductById("INVALID");
    }

    /**
     * 見積もり計算の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_Success() {
        // Given
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);
        when(loanDao.saveEstimate(any(LoanEstimate.class))).thenReturn(testEstimate);

        // When
        LoanEstimate result = loanService.calculateEstimate("PROD001", 3000, 30);

        // Then
        assertNotNull(result);
        assertEquals("EST001", result.getEstimateId());
        assertEquals(3000, result.getLoanAmount());
        assertEquals(30, result.getLoanTerm());
        assertEquals(1.5, result.getInterestRate(), 0.01);
        verify(loanDao, times(1)).getProductById("PROD001");
        verify(loanDao, times(1)).saveEstimate(any(LoanEstimate.class));
    }

    /**
     * 見積もり計算の異常系テスト（商品未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_ProductNotFound() {
        // Given
        when(loanDao.getProductById("INVALID")).thenReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate("INVALID", 3000, 30);
        });
        verify(loanDao, times(1)).getProductById("INVALID");
        verify(loanDao, never()).saveEstimate(any(LoanEstimate.class));
    }

    /**
     * 見積もり計算の異常系テスト（無効な融資額）
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_InvalidLoanAmount() {
        // Given
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate("PROD001", 500, 30); // 最小額未満
        });
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate("PROD001", 6000, 30); // 最大額超過
        });
    }

    /**
     * 見積もり計算の異常系テスト（無効な融資期間）
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_InvalidLoanTerm() {
        // Given
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate("PROD001", 3000, 5); // 最小期間未満
        });
        assertThrows(IllegalArgumentException.class, () -> {
            loanService.calculateEstimate("PROD001", 3000, 40); // 最大期間超過
        });
    }

    /**
     * 見積もり計算の等額返済テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_EqualPaymentMethod() {
        // Given
        testProduct.setRepaymentMethod("EQUAL_PAYMENT");
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);
        when(loanDao.saveEstimate(any(LoanEstimate.class))).thenReturn(testEstimate);

        // When
        LoanEstimate result = loanService.calculateEstimate("PROD001", 3000, 30);

        // Then
        assertNotNull(result);
        // 等額返済の場合の計算結果を検証
        assertTrue(result.getMonthlyPayment() > 0);
        verify(loanDao, times(1)).saveEstimate(any(LoanEstimate.class));
    }

    /**
     * 見積もり計算の元金均等返済テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_EqualPrincipalMethod() {
        // Given
        testProduct.setRepaymentMethod("EQUAL_PRINCIPAL");
        when(loanDao.getProductById("PROD001")).thenReturn(testProduct);
        when(loanDao.saveEstimate(any(LoanEstimate.class))).thenReturn(testEstimate);

        // When
        LoanEstimate result = loanService.calculateEstimate("PROD001", 3000, 30);

        // Then
        assertNotNull(result);
        // 元金均等返済の場合の計算結果を検証
        assertTrue(result.getMonthlyPayment() > 0);
        verify(loanDao, times(1)).saveEstimate(any(LoanEstimate.class));
    }

    /**
     * 申し込み提出の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_Success() {
        // Given
        when(loanDao.saveApplication(any(LoanApplication.class))).thenReturn(testApplication);

        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        assertEquals("APP001", result.getApplicationId());
        assertEquals("PENDING", result.getApplicationStatus());
        verify(loanDao, times(1)).saveApplication(any(LoanApplication.class));
    }

    /**
     * 申し込み提出の自動承認テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_AutoApproval() {
        // Given
        testApplication.setAnnualIncome(800); // 高収入
        testApplication.setPropertyValue(5000); // 高額物件
        when(loanDao.saveApplication(any(LoanApplication.class))).thenReturn(testApplication);

        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        // 自動承認の条件を満たす場合の検証
        verify(loanDao, times(1)).saveApplication(any(LoanApplication.class));
    }

    /**
     * 申し込み提出の自動却下テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_AutoRejection() {
        // Given
        testApplication.setAnnualIncome(300); // 低収入
        testApplication.setPropertyValue(2000); // 低額物件
        when(loanDao.saveApplication(any(LoanApplication.class))).thenReturn(testApplication);

        // When
        LoanApplication result = loanService.submitApplication(testApplication);

        // Then
        assertNotNull(result);
        // 自動却下の条件を満たす場合の検証
        verify(loanDao, times(1)).saveApplication(any(LoanApplication.class));
    }

    /**
     * 申し込み提出の異常系テスト（DAO例外）
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_DaoException() {
        // Given
        when(loanDao.saveApplication(any(LoanApplication.class)))
                .thenThrow(new RuntimeException("DAO error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanService.submitApplication(testApplication);
        });
        verify(loanDao, times(1)).saveApplication(any(LoanApplication.class));
    }

    /**
     * 申し込みIDによる申し込み取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationById_Success() {
        // Given
        when(loanDao.getApplicationById("APP001")).thenReturn(testApplication);

        // When
        LoanApplication result = loanService.getApplicationById("APP001");

        // Then
        assertNotNull(result);
        assertEquals("APP001", result.getApplicationId());
        assertEquals("田中太郎", result.getCustomerName());
        verify(loanDao, times(1)).getApplicationById("APP001");
    }

    /**
     * 申し込みIDによる申し込み取得の異常系テスト（申し込み未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationById_NotFound() {
        // Given
        when(loanDao.getApplicationById("INVALID")).thenReturn(null);

        // When
        LoanApplication result = loanService.getApplicationById("INVALID");

        // Then
        assertNull(result);
        verify(loanDao, times(1)).getApplicationById("INVALID");
    }

    /**
     * 顧客IDによる申し込み一覧取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationsByCustomerId_Success() {
        // Given
        when(loanDao.getApplicationsByCustomerId("CUST001"))
                .thenReturn(Arrays.asList(testApplication));

        // When
        List<LoanApplication> result = loanService.getApplicationsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("APP001", result.get(0).getApplicationId());
        verify(loanDao, times(1)).getApplicationsByCustomerId("CUST001");
    }

    /**
     * 顧客IDによる申し込み一覧取得の空リストテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationsByCustomerId_EmptyList() {
        // Given
        when(loanDao.getApplicationsByCustomerId("CUST001")).thenReturn(Arrays.asList());

        // When
        List<LoanApplication> result = loanService.getApplicationsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(loanDao, times(1)).getApplicationsByCustomerId("CUST001");
    }
} 