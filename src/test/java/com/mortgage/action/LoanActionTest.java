package com.mortgage.action;

import com.mortgage.model.LoanProduct;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanApplication;
import com.mortgage.service.LoanService;
import com.opensymphony.xwork2.ActionSupport;
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
 * LoanActionクラスのユニットテスト
 * IDEで▷ボタンをクリックして個別のテストメソッドを実行できます
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanActionTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanAction loanAction;

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
     * 見積もり表示画面の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testDisplayEstimate_Success() {
        // Given
        when(loanService.getAllProducts()).thenReturn(Arrays.asList(testProduct));

        // When
        String result = loanAction.displayEstimate();

        // Then
        assertEquals(ActionSupport.SUCCESS, result);
        assertNotNull(loanAction.getProducts());
        assertEquals(1, loanAction.getProducts().size());
        assertEquals("PROD001", loanAction.getProducts().get(0).getProductId());
    }

    /**
     * 見積もり表示画面の異常系テスト（サービス例外）
     * ▷ボタンで実行可能
     */
    @Test
    public void testDisplayEstimate_ServiceException() {
        // Given
        when(loanService.getAllProducts()).thenThrow(new RuntimeException("Service error"));

        // When
        String result = loanAction.displayEstimate();

        // Then
        assertEquals(ActionSupport.ERROR, result);
        assertNotNull(loanAction.getActionErrors());
        assertTrue(loanAction.getActionErrors().size() > 0);
    }

    /**
     * 見積もり計算の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_Success() {
        // Given
        loanAction.setProductId("PROD001");
        loanAction.setLoanAmount(3000);
        loanAction.setLoanTerm(30);
        when(loanService.calculateEstimate(anyString(), anyInt(), anyInt())).thenReturn(testEstimate);

        // When
        String result = loanAction.calculateEstimate();

        // Then
        assertEquals(ActionSupport.SUCCESS, result);
        assertNotNull(loanAction.getEstimate());
        assertEquals("EST001", loanAction.getEstimate().getEstimateId());
        assertEquals(3000, loanAction.getEstimate().getLoanAmount());
    }

    /**
     * 見積もり計算の異常系テスト（無効な入力）
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_InvalidInput() {
        // Given
        loanAction.setProductId("");
        loanAction.setLoanAmount(0);
        loanAction.setLoanTerm(0);

        // When
        String result = loanAction.calculateEstimate();

        // Then
        assertEquals(ActionSupport.INPUT, result);
        assertNotNull(loanAction.getFieldErrors());
        assertTrue(loanAction.getFieldErrors().size() > 0);
    }

    /**
     * 見積もり計算の異常系テスト（サービス例外）
     * ▷ボタンで実行可能
     */
    @Test
    public void testCalculateEstimate_ServiceException() {
        // Given
        loanAction.setProductId("PROD001");
        loanAction.setLoanAmount(3000);
        loanAction.setLoanTerm(30);
        when(loanService.calculateEstimate(anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Calculation error"));

        // When
        String result = loanAction.calculateEstimate();

        // Then
        assertEquals(ActionSupport.ERROR, result);
        assertNotNull(loanAction.getActionErrors());
        assertTrue(loanAction.getActionErrors().size() > 0);
    }

    /**
     * 申し込み表示画面の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testDisplayApplication_Success() {
        // Given
        when(loanService.getAllProducts()).thenReturn(Arrays.asList(testProduct));

        // When
        String result = loanAction.displayApplication();

        // Then
        assertEquals(ActionSupport.SUCCESS, result);
        assertNotNull(loanAction.getProducts());
        assertEquals(1, loanAction.getProducts().size());
    }

    /**
     * 申し込み提出の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_Success() {
        // Given
        loanAction.setApplication(testApplication);
        when(loanService.submitApplication(any(LoanApplication.class))).thenReturn(testApplication);

        // When
        String result = loanAction.submitApplication();

        // Then
        assertEquals(ActionSupport.SUCCESS, result);
        assertNotNull(loanAction.getApplication());
        assertEquals("APP001", loanAction.getApplication().getApplicationId());
    }

    /**
     * 申し込み提出の異常系テスト（無効な入力）
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_InvalidInput() {
        // Given
        testApplication.setCustomerName("");
        testApplication.setCustomerEmail("invalid-email");
        loanAction.setApplication(testApplication);

        // When
        String result = loanAction.submitApplication();

        // Then
        assertEquals(ActionSupport.INPUT, result);
        assertNotNull(loanAction.getFieldErrors());
        assertTrue(loanAction.getFieldErrors().size() > 0);
    }

    /**
     * 申し込み提出の異常系テスト（サービス例外）
     * ▷ボタンで実行可能
     */
    @Test
    public void testSubmitApplication_ServiceException() {
        // Given
        loanAction.setApplication(testApplication);
        when(loanService.submitApplication(any(LoanApplication.class)))
                .thenThrow(new RuntimeException("Submission error"));

        // When
        String result = loanAction.submitApplication();

        // Then
        assertEquals(ActionSupport.ERROR, result);
        assertNotNull(loanAction.getActionErrors());
        assertTrue(loanAction.getActionErrors().size() > 0);
    }

    /**
     * 商品リスト取得のテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProducts() {
        // Given
        List<LoanProduct> products = Arrays.asList(testProduct);
        loanAction.setProducts(products);

        // When
        List<LoanProduct> result = loanAction.getProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PROD001", result.get(0).getProductId());
    }

    /**
     * 見積もり取得のテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetEstimate() {
        // Given
        loanAction.setEstimate(testEstimate);

        // When
        LoanEstimate result = loanAction.getEstimate();

        // Then
        assertNotNull(result);
        assertEquals("EST001", result.getEstimateId());
    }

    /**
     * 申し込み取得のテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplication() {
        // Given
        loanAction.setApplication(testApplication);

        // When
        LoanApplication result = loanAction.getApplication();

        // Then
        assertNotNull(result);
        assertEquals("APP001", result.getApplicationId());
    }
} 