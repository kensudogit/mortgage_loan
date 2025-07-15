import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LoanEstimate } from '../types';
import './EstimateResultPage.css';

const EstimateResultPage: React.FC = () => {
  const navigate = useNavigate();
  const [estimate, setEstimate] = useState<LoanEstimate | null>(null);

  useEffect(() => {
    const savedEstimate = sessionStorage.getItem('estimate');
    if (savedEstimate) {
      setEstimate(JSON.parse(savedEstimate));
    }
  }, []);

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('ja-JP').format(amount);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('ja-JP');
  };

  if (!estimate) {
    return (
      <div className="container">
        <div className="result-card">
          <div className="result-header">
            <h2>見積もり情報が見つかりません</h2>
            <p>見積もり計算を実行してください。</p>
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
        <h1>見積もり結果</h1>
        <p>お客様の条件に基づく住宅ローンの見積もり結果です</p>
      </div>

      <div className="result-card">
        <div className="result-header">
          <h2>{estimate.productName}</h2>
          <p>見積もり日時: {estimate.estimatedAt ? formatDate(estimate.estimatedAt) : '現在'}</p>
        </div>

        <div className="estimate-details">
          <div className="detail-box">
            <h4>融資額</h4>
            <div className="value">{formatCurrency(estimate.loanAmount)}</div>
            <div className="unit">万円</div>
          </div>
          <div className="detail-box">
            <h4>融資期間</h4>
            <div className="value">{estimate.loanTerm}</div>
            <div className="unit">年</div>
          </div>
          <div className="detail-box">
            <h4>金利</h4>
            <div className="value">{estimate.interestRate}</div>
            <div className="unit">%</div>
          </div>
          <div className="detail-box">
            <h4>月次返済額</h4>
            <div className="value">{formatCurrency(estimate.monthlyPayment)}</div>
            <div className="unit">円</div>
          </div>
        </div>

        <div className="payment-breakdown">
          <h3>返済内訳</h3>
          <div className="breakdown-item">
            <span className="breakdown-label">融資元本</span>
            <span className="breakdown-value">{formatCurrency(estimate.loanAmount)}万円</span>
          </div>
          <div className="breakdown-item">
            <span className="breakdown-label">総利息</span>
            <span className="breakdown-value">{estimate.totalInterest ? formatCurrency(estimate.totalInterest) : '計算中'}円</span>
          </div>
          <div className="breakdown-item">
            <span className="breakdown-label">総返済額</span>
            <span className="breakdown-value">{estimate.totalPayment ? formatCurrency(estimate.totalPayment) : '計算中'}円</span>
          </div>
        </div>

        <div className="info-box">
          <h4>📋 重要なお知らせ</h4>
          <p>
            この見積もりは参考値です。実際の融資条件は審査結果により変動する場合があります。
            詳細な条件については、お申し込み後に正式にお知らせいたします。
          </p>
        </div>

        <div className="action-buttons">
          <button onClick={() => navigate('/application')} className="btn btn-primary">
            この条件で申し込む
          </button>
          <button onClick={() => navigate('/estimate')} className="btn btn-secondary">
            条件を変更する
          </button>
        </div>
      </div>

      <div className="back-link">
        <a href="/">← トップページに戻る</a>
      </div>
    </div>
  );
};

export default EstimateResultPage; 