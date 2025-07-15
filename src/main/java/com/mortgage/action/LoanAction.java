package com.mortgage.action;

import com.mortgage.model.LoanApplication;
import com.mortgage.model.LoanEstimate;
import com.mortgage.model.LoanProduct;
import com.mortgage.service.LoanService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * 住宅ローン関連のActionクラス
 */
@Namespace("/loan")
public class LoanAction extends ActionSupport {

    @Autowired
    private LoanService loanService;

    // 見積もり関連
    private LoanEstimate estimate;
    private List<LoanProduct> products;
    private String selectedProductId;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private BigDecimal interestRate;

    // 申し込み関連
    private LoanApplication application;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    /**
     * 見積もり画面の初期表示
     */
    @Action(value = "estimate", results = {
        @Result(name = SUCCESS, location = "/WEB-INF/jsp/loan/estimate.jsp"),
        @Result(name = ERROR, location = "/WEB-INF/jsp/error.jsp")
    })
    public String estimate() {
        try {
            products = loanService.getAllLoanProducts();
            return SUCCESS;
        } catch (Exception e) {
            addActionError("見積もり画面の表示に失敗しました: " + e.getMessage());
            return ERROR;
        }
    }

    /**
     * 見積もり計算
     */
    @Action(value = "calculate", results = {
        @Result(name = SUCCESS, location = "/WEB-INF/jsp/loan/estimate_result.jsp"),
        @Result(name = ERROR, location = "/WEB-INF/jsp/loan/estimate.jsp")
    })
    public String calculate() {
        try {
            if (loanAmount == null || loanTerm == null || selectedProductId == null) {
                addActionError("必要な情報が入力されていません。");
                return ERROR;
            }

            estimate = loanService.calculateEstimate(selectedProductId, loanAmount, loanTerm);
            return SUCCESS;
        } catch (Exception e) {
            addActionError("見積もり計算に失敗しました: " + e.getMessage());
            return ERROR;
        }
    }

    /**
     * 申し込み画面の初期表示
     */
    @Action(value = "apply", results = {
        @Result(name = SUCCESS, location = "/WEB-INF/jsp/loan/application.jsp"),
        @Result(name = ERROR, location = "/WEB-INF/jsp/error.jsp")
    })
    public String apply() {
        try {
            if (estimate == null) {
                addActionError("見積もり情報がありません。");
                return ERROR;
            }
            return SUCCESS;
        } catch (Exception e) {
            addActionError("申し込み画面の表示に失敗しました: " + e.getMessage());
            return ERROR;
        }
    }

    /**
     * 申し込み処理
     */
    @Action(value = "submit", results = {
        @Result(name = SUCCESS, location = "/WEB-INF/jsp/loan/application_complete.jsp"),
        @Result(name = ERROR, location = "/WEB-INF/jsp/loan/application.jsp")
    })
    public String submit() {
        try {
            if (application == null) {
                addActionError("申し込み情報が入力されていません。");
                return ERROR;
            }

            // 申し込み情報の保存
            application = loanService.submitApplication(application);
            return SUCCESS;
        } catch (Exception e) {
            addActionError("申し込み処理に失敗しました: " + e.getMessage());
            return ERROR;
        }
    }

    // Getter and Setter methods
    public LoanEstimate getEstimate() {
        return estimate;
    }

    public void setEstimate(LoanEstimate estimate) {
        this.estimate = estimate;
    }

    public List<LoanProduct> getProducts() {
        return products;
    }

    public void setProducts(List<LoanProduct> products) {
        this.products = products;
    }

    public String getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(String selectedProductId) {
        this.selectedProductId = selectedProductId;
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

    public LoanApplication getApplication() {
        return application;
    }

    public void setApplication(LoanApplication application) {
        this.application = application;
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
} 