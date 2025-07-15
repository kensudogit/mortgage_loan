import React from 'react';
import { useNavigate } from 'react-router-dom';
import './ErrorPage.css';

const ErrorPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="container">
      <div className="error-card">
        <div className="error-header">
          <div className="error-icon">⚠️</div>
          <h1>エラーが発生しました</h1>
          <p>申し訳ございませんが、システムでエラーが発生しました。</p>
        </div>

        <div className="error-content">
          <div className="error-message">
            <h3>考えられる原因</h3>
            <ul>
              <li>インターネット接続に問題がある</li>
              <li>サーバーが一時的に利用できない</li>
              <li>システムメンテナンス中</li>
              <li>入力データに問題がある</li>
            </ul>
          </div>

          <div className="error-suggestions">
            <h3>対処方法</h3>
            <div className="suggestion-grid">
              <div className="suggestion-item">
                <h4>🔄 ページを再読み込み</h4>
                <p>ブラウザの更新ボタンを押して、ページを再読み込みしてください。</p>
              </div>
              <div className="suggestion-item">
                <h4>⏰ しばらく待つ</h4>
                <p>数分待ってから再度アクセスしてください。</p>
              </div>
              <div className="suggestion-item">
                <h4>🏠 トップページに戻る</h4>
                <p>トップページから再度操作を開始してください。</p>
              </div>
              <div className="suggestion-item">
                <h4>📞 お問い合わせ</h4>
                <p>問題が解決しない場合は、お問い合わせください。</p>
              </div>
            </div>
          </div>

          <div className="contact-info">
            <h4>📞 お問い合わせ先</h4>
            <p>
              電話: 0123-456-789<br />
              メール: support@mortgage-loan.com<br />
              受付時間: 平日 9:00-18:00
            </p>
          </div>
        </div>

        <div className="action-buttons">
          <button onClick={() => window.location.reload()} className="btn">
            ページを再読み込み
          </button>
          <button onClick={() => navigate('/')} className="btn btn-secondary">
            トップページに戻る
          </button>
        </div>
      </div>
    </div>
  );
};

export default ErrorPage; 