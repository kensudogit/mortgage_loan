import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LoanApplication } from '../types';
import './ApplicationCompletePage.css';

const ApplicationCompletePage: React.FC = () => {
  const navigate = useNavigate();
  const [application, setApplication] = useState<LoanApplication | null>(null);

  useEffect(() => {
    const savedApplication = sessionStorage.getItem('application');
    if (savedApplication) {
      setApplication(JSON.parse(savedApplication));
    }
  }, []);

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('ja-JP').format(amount);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('ja-JP');
  };

  const getStatusLabel = (status: string) => {
    switch (status) {
      case 'PENDING': return '審査中';
      case 'APPROVED': return '承認済み';
      case 'REJECTED': return '却下';
      case 'PROCESSING': return '処理中';
      default: return status;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'APPROVED': return 'success';
      case 'REJECTED': return 'error';
      case 'PENDING': return 'warning';
      default: return 'info';
    }
  };

  if (!application) {
    return (
      <div className="container">
        <div className="complete-card">
          <div className="complete-header">
            <h2>申し込み情報が見つかりません</h2>
            <p>申し込み手続きを実行してください。</p>
          </div>
          <div className="action-buttons">
            <button onClick={() => navigate('/application')} className="btn">
              申し込み画面に戻る
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="header">
        <h1>申し込み完了</h1>
        <p>住宅ローンの申し込みが完了しました</p>
      </div>

      <div className="complete-card">
        <div className="complete-header">
          <div className="success-icon">✅</div>
          <h2>申し込みが完了しました</h2>
          <p>お客様の申し込みを受け付けました。審査結果はメールでお知らせいたします。</p>
        </div>

        <div className="application-details">
          <h3>申し込み内容</h3>
          <div className="details-grid">
            <div className="detail-item">
              <span className="label">申し込み番号:</span>
              <span className="value">{application.applicationId}</span>
            </div>
            <div className="detail-item">
              <span className="label">申し込み日:</span>
              <span className="value">
                {application.applicationDate ? formatDate(application.applicationDate) : '現在'}
              </span>
            </div>
            <div className="detail-item">
              <span className="label">商品名:</span>
              <span className="value">{application.productName}</span>
            </div>
            <div className="detail-item">
              <span className="label">融資額:</span>
              <span className="value">{formatCurrency(application.loanAmount)}万円</span>
            </div>
            <div className="detail-item">
              <span className="label">融資期間:</span>
              <span className="value">{application.loanTerm}年</span>
            </div>
            <div className="detail-item">
              <span className="label">月次返済額:</span>
              <span className="value">{formatCurrency(application.monthlyPayment || 0)}円</span>
            </div>
            <div className="detail-item">
              <span className="label">お客様名:</span>
              <span className="value">{application.customerName}</span>
            </div>
            <div className="detail-item">
              <span className="label">メールアドレス:</span>
              <span className="value">{application.customerEmail}</span>
            </div>
          </div>
        </div>

        <div className={`status-box status-${getStatusColor(application.applicationStatus || 'PENDING')}`}>
          <h4>審査状況</h4>
          <div className="status-content">
            <span className="status-label">
              {getStatusLabel(application.applicationStatus || 'PENDING')}
            </span>
            {application.rejectionReason && (
              <p className="rejection-reason">
                却下理由: {application.rejectionReason}
              </p>
            )}
          </div>
        </div>

        <div className="info-box">
          <h4>📧 今後の流れ</h4>
          <div className="flow-steps">
            <div className="step">
              <div className="step-number">1</div>
              <div className="step-content">
                <h5>申し込み受付</h5>
                <p>申し込み内容の確認メールをお送りします</p>
              </div>
            </div>
            <div className="step">
              <div className="step-number">2</div>
              <div className="step-content">
                <h5>書類提出</h5>
                <p>必要書類の提出をお願いします</p>
              </div>
            </div>
            <div className="step">
              <div className="step-number">3</div>
              <div className="step-content">
                <h5>審査</h5>
                <p>審査結果をメールでお知らせします</p>
              </div>
            </div>
            <div className="step">
              <div className="step-number">4</div>
              <div className="step-content">
                <h5>契約</h5>
                <p>承認の場合は契約手続きを行います</p>
              </div>
            </div>
          </div>
        </div>

        <div className="contact-info">
          <h4>📞 お問い合わせ</h4>
          <p>
            ご不明な点がございましたら、お気軽にお問い合わせください。<br />
            電話: 0123-456-789<br />
            メール: support@mortgage-loan.com
          </p>
        </div>

        <div className="action-buttons">
          <button onClick={() => navigate('/')} className="btn">
            トップページに戻る
          </button>
          <button onClick={() => navigate('/estimate')} className="btn btn-secondary">
            新しい見積もり
          </button>
        </div>
      </div>
    </div>
  );
};

export default ApplicationCompletePage; 