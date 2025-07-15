package com.mortgage.service;

import com.mortgage.model.LoanApplication;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanProduct;
import com.mortgage.dao.LoanDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * LoanServiceの単体テスト
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class LoanServiceTest {

    @Mock
    private LoanDao loanDao;

    @InjectMocks
    private LoanService loanService;

    private LoanProduct testProduct;
    private LoanApplication testApplication;

    @Before
    public void setUp() {
        // テスト用のローン商品を作成
        testProduct = new LoanProduct();
        testProduct.setProductId("TEST_001");
        testProduct.setProductName("テスト商品");
        testProduct.setProductType("FIXED");
        testProduct.setCurrentInterestRate(new BigDecimal("0.85"));
        testProduct.setMinLoanAmount(100);
        testProduct.setMaxLoanAmount(5000);
        testProduct.setMinLoanTerm(10);
        testProduct.setMaxLoanTerm(35);
        testProduct.setRepaymentMethod("EQUAL_PAYMENT");

        // テスト用の申し込みを作成
        testApplication = new LoanApplication();
        testApplication.setCustomerName("テスト太郎");
        testApplication.setCustomerEmail("test@example.com");
        testApplication.setCustomerPhone("090-1234-5678");
        testApplication.setProductId("TEST_001");
        testApplication.setLoanAmount(new BigDecimal("3000"));
        testApplication.setLoanTerm(30);
        testApplication.setPropertyAddress("東京都渋谷区テスト1-2-3");
        testApplication.setPropertyType("DETACHED");
        testApplication.setPropertyValue(new BigDecimal("3500"));
        testApplication.setEmploymentType("SALARIED");
        testApplication.setAnnualIncome(new BigDecimal("500"));
        testApplication.setBankName("テスト銀行");
        testApplication.setBranchName("渋谷支店");
        testApplication.setBankAccountNumber("1234567");
    }

    @Test
    public void testGetAllLoanProducts() {
        // モックの設定
        List<LoanProduct> expectedProducts = Arrays.asList(testProduct);
        when(loanDao.getAllLoanProducts()).thenReturn(expectedProducts);

        // テスト実行
        List<LoanProduct> result = loanService.getAllLoanProducts();

        // 検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST_001", result.get(0).getProductId());
        verify(loanDao, times(1)).getAllLoanProducts();
    }

    @Test
    public void testGetLoanProductById() {
        // モックの設定
        when(loanDao.getLoanProductById("TEST_001")).thenReturn(testProduct);

        // テスト実行
        LoanProduct result = loanService.getLoanProductById("TEST_001");

        // 検証
        assertNotNull(result);
        assertEquals("TEST_001", result.getProductId());
        assertEquals("テスト商品", result.getProductName());
        verify(loanDao, times(1)).getLoanProductById("TEST_001");
    }

    @Test
    public void testCalculateEstimate() {
        // モックの設定
        when(loanDao.getLoanProductById("TEST_001")).thenReturn(testProduct);
        doNothing().when(loanDao).saveEstimate(any(LoanEstimate.class));

        // テスト実行
        LoanEstimate result = loanService.calculateEstimate("TEST_001", new BigDecimal("3000"), 30);

        // 検証
        assertNotNull(result);
        assertEquals("TEST_001", result.getProductId());
        assertEquals("テスト商品", result.getProductName());
        assertEquals(new BigDecimal("3000"), result.getLoanAmount());
        assertEquals(Integer.valueOf(30), result.getLoanTerm());
        assertEquals(new BigDecimal("0.85"), result.getInterestRate());
        assertNotNull(result.getMonthlyPayment());
        assertTrue(result.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        verify(loanDao, times(1)).getLoanProductById("TEST_001");
        verify(loanDao, times(1)).saveEstimate(any(LoanEstimate.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateEstimateWithInvalidProduct() {
        // モックの設定
        when(loanDao.getLoanProductById("INVALID")).thenReturn(null);

        // テスト実行（例外が発生することを期待）
        loanService.calculateEstimate("INVALID", new BigDecimal("3000"), 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateEstimateWithInvalidLoanAmount() {
        // モックの設定
        when(loanDao.getLoanProductById("TEST_001")).thenReturn(testProduct);

        // テスト実行（融資額が最小額を下回る）
        loanService.calculateEstimate("TEST_001", new BigDecimal("50"), 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateEstimateWithInvalidLoanTerm() {
        // モックの設定
        when(loanDao.getLoanProductById("TEST_001")).thenReturn(testProduct);

        // テスト実行（融資期間が最小期間を下回る）
        loanService.calculateEstimate("TEST_001", new BigDecimal("3000"), 5);
    }

    @Test
    public void testSubmitApplication() {
        // モックの設定
        doNothing().when(loanDao).saveApplication(any(LoanApplication.class));
        doNothing().when(loanDao).updateApplication(any(LoanApplication.class));

        // テスト実行
        LoanApplication result = loanService.submitApplication(testApplication);

        // 検証
        assertNotNull(result);
        assertNotNull(result.getApplicationId());
        assertTrue(result.getApplicationId().startsWith("APP"));
        assertEquals("PENDING", result.getApplicationStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        verify(loanDao, times(1)).saveApplication(any(LoanApplication.class));
        verify(loanDao, times(1)).updateApplication(any(LoanApplication.class));
    }

    @Test
    public void testGetApplicationById() {
        // モックの設定
        when(loanDao.getApplicationById("APP123")).thenReturn(testApplication);

        // テスト実行
        LoanApplication result = loanService.getApplicationById("APP123");

        // 検証
        assertNotNull(result);
        assertEquals("テスト太郎", result.getCustomerName());
        verify(loanDao, times(1)).getApplicationById("APP123");
    }

    @Test
    public void testGetApplicationsByCustomerId() {
        // モックの設定
        List<LoanApplication> expectedApplications = Arrays.asList(testApplication);
        when(loanDao.getApplicationsByCustomerId("CUST001")).thenReturn(expectedApplications);

        // テスト実行
        List<LoanApplication> result = loanService.getApplicationsByCustomerId("CUST001");

        // 検証
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("テスト太郎", result.get(0).getCustomerName());
        verify(loanDao, times(1)).getApplicationsByCustomerId("CUST001");
    }

    @Test
    public void testMonthlyPaymentCalculation() {
        // 月次返済額の計算をテスト
        BigDecimal principal = new BigDecimal("3000");
        BigDecimal annualRate = new BigDecimal("0.85");
        Integer years = 30;

        // テスト実行
        LoanEstimate estimate = loanService.calculateEstimate("TEST_001", principal, years);

        // 検証
        assertNotNull(estimate.getMonthlyPayment());
        assertTrue(estimate.getMonthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        
        // 総返済額の検証
        assertNotNull(estimate.getTotalPayment());
        assertTrue(estimate.getTotalPayment().compareTo(principal) > 0);
        
        // 総利息の検証
        assertNotNull(estimate.getTotalInterest());
        assertTrue(estimate.getTotalInterest().compareTo(BigDecimal.ZERO) > 0);
    }
} 