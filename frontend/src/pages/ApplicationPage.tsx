import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { applicationApi } from '../services/api';
import { LoanApplication, LoanEstimate } from '../types';
import './ApplicationPage.css';

const ApplicationPage: React.FC = () => {
  const navigate = useNavigate();
  const [estimate, setEstimate] = useState<LoanEstimate | null>(null);
  const [formData, setFormData] = useState<Partial<LoanApplication>>({
    customerName: '',
    customerEmail: '',
    customerPhone: '',
    propertyAddress: '',
    propertyType: 'DETACHED',
    propertyValue: 0,
    employmentType: 'SALARIED',
    annualIncome: 0,
    bankAccountNumber: '',
    bankName: '',
    branchName: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const savedEstimate = sessionStorage.getItem('estimate');
    if (savedEstimate) {
      const estimateData = JSON.parse(savedEstimate);
      setEstimate(estimateData);
      setFormData(prev => ({
        ...prev,
        productId: estimateData.productId,
        productName: estimateData.productName,
        loanAmount: estimateData.loanAmount,
        loanTerm: estimateData.loanTerm,
        interestRate: estimateData.interestRate,
        monthlyPayment: estimateData.monthlyPayment
      }));
    }
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleNumberInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: parseFloat(value) || 0
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!estimate) {
      setError('見積もり情報が見つかりません');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const application: LoanApplication = {
        ...formData as LoanApplication,
        productId: estimate.productId,
        productName: estimate.productName,
        loanAmount: estimate.loanAmount,
        loanTerm: estimate.loanTerm,
        interestRate: estimate.interestRate,
        monthlyPayment: estimate.monthlyPayment
      };

      const result = await applicationApi.submit(application);
      
      // 申し込み結果をセッションストレージに保存
      sessionStorage.setItem('application', JSON.stringify(result));
      
      navigate('/application/complete');
    } catch (err) {
      setError(err instanceof Error ? err.message : '申し込みに失敗しました');
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('ja-JP').format(amount);
  };

  if (!estimate) {
    return (
      <div className="container">
        <div className="form-card">
          <div className="error-message">
            見積もり情報が見つかりません。先に見積もり計算を実行してください。
          </div>
          <div className="action-buttons">
            <button onClick={() => navigate('/estimate')} className="btn">
              見積もり画面に戻る
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="header">
        <h1>住宅ローン申し込み</h1>
        <p>お客様の情報を入力して、住宅ローンの申し込みを行います</p>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="form-card">
        <div className="estimate-summary">
          <h3>申し込み条件</h3>
          <div className="summary-grid">
            <div className="summary-item">
              <span className="label">商品名:</span>
              <span className="value">{estimate.productName}</span>
            </div>
            <div className="summary-item">
              <span className="label">融資額:</span>
              <span className="value">{formatCurrency(estimate.loanAmount)}万円</span>
            </div>
            <div className="summary-item">
              <span className="label">融資期間:</span>
              <span className="value">{estimate.loanTerm}年</span>
            </div>
            <div className="summary-item">
              <span className="label">月次返済額:</span>
              <span className="value">{formatCurrency(estimate.monthlyPayment)}円</span>
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-section">
            <h3>お客様情報</h3>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="customerName">お名前 *</label>
                <input
                  type="text"
                  id="customerName"
                  name="customerName"
                  value={formData.customerName}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="customerEmail">メールアドレス *</label>
                <input
                  type="email"
                  id="customerEmail"
                  name="customerEmail"
                  value={formData.customerEmail}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                />
              </div>
            </div>
            <div className="form-group">
              <label htmlFor="customerPhone">電話番号 *</label>
              <input
                type="tel"
                id="customerPhone"
                name="customerPhone"
                value={formData.customerPhone}
                onChange={handleInputChange}
                className="form-control"
                required
              />
            </div>
          </div>

          <div className="form-section">
            <h3>物件情報</h3>
            <div className="form-group">
              <label htmlFor="propertyAddress">物件住所 *</label>
              <input
                type="text"
                id="propertyAddress"
                name="propertyAddress"
                value={formData.propertyAddress}
                onChange={handleInputChange}
                className="form-control"
                required
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="propertyType">物件種別 *</label>
                <select
                  id="propertyType"
                  name="propertyType"
                  value={formData.propertyType}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                >
                  <option value="DETACHED">一戸建て</option>
                  <option value="APARTMENT">マンション</option>
                  <option value="CONDO">分譲マンション</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="propertyValue">物件価格（万円） *</label>
                <input
                  type="number"
                  id="propertyValue"
                  name="propertyValue"
                  value={formData.propertyValue}
                  onChange={handleNumberInputChange}
                  className="form-control"
                  min="100"
                  required
                />
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>収入・職業情報</h3>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="employmentType">職業 *</label>
                <select
                  id="employmentType"
                  name="employmentType"
                  value={formData.employmentType}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                >
                  <option value="SALARIED">会社員</option>
                  <option value="SELF_EMPLOYED">自営業</option>
                  <option value="BUSINESS_OWNER">経営者</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="annualIncome">年収（万円） *</label>
                <input
                  type="number"
                  id="annualIncome"
                  name="annualIncome"
                  value={formData.annualIncome}
                  onChange={handleNumberInputChange}
                  className="form-control"
                  min="200"
                  required
                />
              </div>
            </div>
          </div>

          <div className="form-section">
            <h3>口座情報</h3>
            <div className="form-group">
              <label htmlFor="bankName">銀行名 *</label>
              <input
                type="text"
                id="bankName"
                name="bankName"
                value={formData.bankName}
                onChange={handleInputChange}
                className="form-control"
                required
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="branchName">支店名 *</label>
                <input
                  type="text"
                  id="branchName"
                  name="branchName"
                  value={formData.branchName}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="bankAccountNumber">口座番号 *</label>
                <input
                  type="text"
                  id="bankAccountNumber"
                  name="bankAccountNumber"
                  value={formData.bankAccountNumber}
                  onChange={handleInputChange}
                  className="form-control"
                  required
                />
              </div>
            </div>
          </div>

          <div className="info-box">
            <h4>📋 重要なお知らせ</h4>
            <p>
              お客様の個人情報は厳重に管理し、住宅ローンの審査目的以外には使用いたしません。
              申し込み後、審査結果はメールでお知らせいたします。
            </p>
          </div>

          <div className="action-buttons">
            <button type="submit" className="btn" disabled={loading}>
              {loading ? '申し込み中...' : '申し込みを確定する'}
            </button>
            <button type="button" onClick={() => navigate('/estimate')} className="btn btn-secondary">
              条件を変更する
            </button>
          </div>
        </form>
      </div>

      <div className="back-link">
        <a href="/">← トップページに戻る</a>
      </div>
    </div>
  );
};

export default ApplicationPage; 