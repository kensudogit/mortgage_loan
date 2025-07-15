package com.mortgage.dao;

import com.mortgage.model.LoanProduct;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanApplication;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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
 * LoanDaoImplクラスのユニットテスト
 * IDEで▷ボタンをクリックして個別のテストメソッドを実行できます
 */
@RunWith(MockitoJUnitRunner.class)
public class LoanDaoImplTest {

    @Mock
    private SqlSessionFactory sqlSessionFactory;

    @Mock
    private SqlSession sqlSession;

    @InjectMocks
    private LoanDaoImpl loanDao;

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
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanProductMapper.selectAll")).thenReturn(Arrays.asList(testProduct));
        doNothing().when(sqlSession).close();

        // When
        List<LoanProduct> result = loanDao.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PROD001", result.get(0).getProductId());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanProductMapper.selectAll");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 全商品取得の空リストテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_EmptyList() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanProductMapper.selectAll")).thenReturn(Arrays.asList());
        doNothing().when(sqlSession).close();

        // When
        List<LoanProduct> result = loanDao.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanProductMapper.selectAll");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 全商品取得の異常系テスト（例外発生）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllProducts_Exception() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanProductMapper.selectAll")).thenThrow(new RuntimeException("Database error"));
        doNothing().when(sqlSession).close();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanDao.getAllProducts();
        });
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanProductMapper.selectAll");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 商品IDによる商品取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProductById_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanProductMapper.selectById", "PROD001")).thenReturn(testProduct);
        doNothing().when(sqlSession).close();

        // When
        LoanProduct result = loanDao.getProductById("PROD001");

        // Then
        assertNotNull(result);
        assertEquals("PROD001", result.getProductId());
        assertEquals("固定金利住宅ローン", result.getProductName());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanProductMapper.selectById", "PROD001");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 商品IDによる商品取得の異常系テスト（商品未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetProductById_NotFound() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanProductMapper.selectById", "INVALID")).thenReturn(null);
        doNothing().when(sqlSession).close();

        // When
        LoanProduct result = loanDao.getProductById("INVALID");

        // Then
        assertNull(result);
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanProductMapper.selectById", "INVALID");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 見積もり保存の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSaveEstimate_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.insert("LoanEstimateMapper.insert", testEstimate)).thenReturn(1);
        doNothing().when(sqlSession).commit();
        doNothing().when(sqlSession).close();

        // When
        LoanEstimate result = loanDao.saveEstimate(testEstimate);

        // Then
        assertNotNull(result);
        assertEquals("EST001", result.getEstimateId());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).insert("LoanEstimateMapper.insert", testEstimate);
        verify(sqlSession, times(1)).commit();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 見積もり保存の異常系テスト（挿入失敗）
     * ▷ボタンで実行可能
     */
    @Test
    public void testSaveEstimate_InsertFailure() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.insert("LoanEstimateMapper.insert", testEstimate)).thenReturn(0);
        doNothing().when(sqlSession).rollback();
        doNothing().when(sqlSession).close();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanDao.saveEstimate(testEstimate);
        });
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).insert("LoanEstimateMapper.insert", testEstimate);
        verify(sqlSession, times(1)).rollback();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 見積もりIDによる見積もり取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetEstimateById_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanEstimateMapper.selectById", "EST001")).thenReturn(testEstimate);
        doNothing().when(sqlSession).close();

        // When
        LoanEstimate result = loanDao.getEstimateById("EST001");

        // Then
        assertNotNull(result);
        assertEquals("EST001", result.getEstimateId());
        assertEquals("PROD001", result.getProductId());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanEstimateMapper.selectById", "EST001");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 見積もりIDによる見積もり取得の異常系テスト（見積もり未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetEstimateById_NotFound() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanEstimateMapper.selectById", "INVALID")).thenReturn(null);
        doNothing().when(sqlSession).close();

        // When
        LoanEstimate result = loanDao.getEstimateById("INVALID");

        // Then
        assertNull(result);
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanEstimateMapper.selectById", "INVALID");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込み保存の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testSaveApplication_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.insert("LoanApplicationMapper.insert", testApplication)).thenReturn(1);
        doNothing().when(sqlSession).commit();
        doNothing().when(sqlSession).close();

        // When
        LoanApplication result = loanDao.saveApplication(testApplication);

        // Then
        assertNotNull(result);
        assertEquals("APP001", result.getApplicationId());
        assertEquals("PENDING", result.getApplicationStatus());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).insert("LoanApplicationMapper.insert", testApplication);
        verify(sqlSession, times(1)).commit();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込み保存の異常系テスト（挿入失敗）
     * ▷ボタンで実行可能
     */
    @Test
    public void testSaveApplication_InsertFailure() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.insert("LoanApplicationMapper.insert", testApplication)).thenReturn(0);
        doNothing().when(sqlSession).rollback();
        doNothing().when(sqlSession).close();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanDao.saveApplication(testApplication);
        });
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).insert("LoanApplicationMapper.insert", testApplication);
        verify(sqlSession, times(1)).rollback();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込みIDによる申し込み取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationById_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanApplicationMapper.selectById", "APP001")).thenReturn(testApplication);
        doNothing().when(sqlSession).close();

        // When
        LoanApplication result = loanDao.getApplicationById("APP001");

        // Then
        assertNotNull(result);
        assertEquals("APP001", result.getApplicationId());
        assertEquals("田中太郎", result.getCustomerName());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanApplicationMapper.selectById", "APP001");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込みIDによる申し込み取得の異常系テスト（申し込み未発見）
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationById_NotFound() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectOne("LoanApplicationMapper.selectById", "INVALID")).thenReturn(null);
        doNothing().when(sqlSession).close();

        // When
        LoanApplication result = loanDao.getApplicationById("INVALID");

        // Then
        assertNull(result);
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectOne("LoanApplicationMapper.selectById", "INVALID");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 顧客IDによる申し込み一覧取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationsByCustomerId_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanApplicationMapper.selectByCustomerId", "CUST001"))
                .thenReturn(Arrays.asList(testApplication));
        doNothing().when(sqlSession).close();

        // When
        List<LoanApplication> result = loanDao.getApplicationsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("APP001", result.get(0).getApplicationId());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanApplicationMapper.selectByCustomerId", "CUST001");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 顧客IDによる申し込み一覧取得の空リストテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetApplicationsByCustomerId_EmptyList() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanApplicationMapper.selectByCustomerId", "CUST001"))
                .thenReturn(Arrays.asList());
        doNothing().when(sqlSession).close();

        // When
        List<LoanApplication> result = loanDao.getApplicationsByCustomerId("CUST001");

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanApplicationMapper.selectByCustomerId", "CUST001");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込み更新の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testUpdateApplication_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.update("LoanApplicationMapper.update", testApplication)).thenReturn(1);
        doNothing().when(sqlSession).commit();
        doNothing().when(sqlSession).close();

        // When
        loanDao.updateApplication(testApplication);

        // Then
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).update("LoanApplicationMapper.update", testApplication);
        verify(sqlSession, times(1)).commit();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 申し込み更新の異常系テスト（更新失敗）
     * ▷ボタンで実行可能
     */
    @Test
    public void testUpdateApplication_UpdateFailure() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.update("LoanApplicationMapper.update", testApplication)).thenReturn(0);
        doNothing().when(sqlSession).rollback();
        doNothing().when(sqlSession).close();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            loanDao.updateApplication(testApplication);
        });
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).update("LoanApplicationMapper.update", testApplication);
        verify(sqlSession, times(1)).rollback();
        verify(sqlSession, times(1)).close();
    }

    /**
     * 全申し込み取得の正常系テスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllApplications_Success() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanApplicationMapper.selectAll"))
                .thenReturn(Arrays.asList(testApplication));
        doNothing().when(sqlSession).close();

        // When
        List<LoanApplication> result = loanDao.getAllApplications();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("APP001", result.get(0).getApplicationId());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanApplicationMapper.selectAll");
        verify(sqlSession, times(1)).close();
    }

    /**
     * 全申し込み取得の空リストテスト
     * ▷ボタンで実行可能
     */
    @Test
    public void testGetAllApplications_EmptyList() {
        // Given
        when(sqlSessionFactory.openSession()).thenReturn(sqlSession);
        when(sqlSession.selectList("LoanApplicationMapper.selectAll")).thenReturn(Arrays.asList());
        doNothing().when(sqlSession).close();

        // When
        List<LoanApplication> result = loanDao.getAllApplications();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sqlSessionFactory, times(1)).openSession();
        verify(sqlSession, times(1)).selectList("LoanApplicationMapper.selectAll");
        verify(sqlSession, times(1)).close();
    }
} 