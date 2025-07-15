package com.mortgage.dao.impl;

import com.mortgage.dao.LoanDao;
import com.mortgage.model.LoanApplication;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanProduct;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 住宅ローンDAO実装クラス
 */
@Repository
public class LoanDaoImpl implements LoanDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<LoanProduct> getAllLoanProducts() {
        return sqlSession.selectList("LoanMapper.getAllLoanProducts");
    }

    @Override
    public LoanProduct getLoanProductById(String productId) {
        return sqlSession.selectOne("LoanMapper.getLoanProductById", productId);
    }

    @Override
    public void saveEstimate(LoanEstimate estimate) {
        sqlSession.insert("LoanMapper.saveEstimate", estimate);
    }

    @Override
    public LoanEstimate getEstimateById(String estimateId) {
        return sqlSession.selectOne("LoanMapper.getEstimateById", estimateId);
    }

    @Override
    public void saveApplication(LoanApplication application) {
        sqlSession.insert("LoanMapper.saveApplication", application);
    }

    @Override
    public void updateApplication(LoanApplication application) {
        sqlSession.update("LoanMapper.updateApplication", application);
    }

    @Override
    public LoanApplication getApplicationById(String applicationId) {
        return sqlSession.selectOne("LoanMapper.getApplicationById", applicationId);
    }

    @Override
    public List<LoanApplication> getApplicationsByCustomerId(String customerId) {
        return sqlSession.selectList("LoanMapper.getApplicationsByCustomerId", customerId);
    }

    @Override
    public List<LoanApplication> getApplicationsByStatus(String status) {
        return sqlSession.selectList("LoanMapper.getApplicationsByStatus", status);
    }
} 