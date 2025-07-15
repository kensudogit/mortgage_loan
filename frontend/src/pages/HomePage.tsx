import React from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';

const HomePage: React.FC = () => {
  return (
    <div className="container">
      <div className="header">
        <h1>住宅ローンシステム</h1>
        <p>安心・安全な住宅ローンサービスを提供いたします</p>
      </div>

      <div className="main-content">
        <div className="card">
          <h3>🏠 ローン見積もり</h3>
          <p>お客様の条件に合わせて、最適な住宅ローンの見積もりを計算いたします。金利や返済期間を自由に設定できます。</p>
          <Link to="/estimate" className="btn">
            見積もりを始める
          </Link>
        </div>

        <div className="card">
          <h3>📋 ローン申し込み</h3>
          <p>見積もり結果を基に、簡単な手続きで住宅ローンの申し込みができます。審査状況も随時確認できます。</p>
          <Link to="/application" className="btn">
            申し込み手続き
          </Link>
        </div>

        <div className="card">
          <h3>📊 返済シミュレーション</h3>
          <p>様々な返済プランをシミュレーションして、お客様に最適な返済方法を見つけることができます。</p>
          <Link to="/estimate" className="btn">
            シミュレーション
          </Link>
        </div>
      </div>

      <div className="features">
        <h2>システムの特徴</h2>
        <div className="feature-grid">
          <div className="feature-item">
            <h4>🎯 正確な計算</h4>
            <p>最新の金利情報を基に、正確な返済額を計算します</p>
          </div>
          <div className="feature-item">
            <h4>⚡ 迅速な処理</h4>
            <p>自動審査システムにより、迅速な審査結果をお届けします</p>
          </div>
          <div className="feature-item">
            <h4>🔒 セキュリティ</h4>
            <p>お客様の個人情報は厳重に管理し、安全に取り扱います</p>
          </div>
          <div className="feature-item">
            <h4>📱 使いやすさ</h4>
            <p>直感的な操作で、誰でも簡単に利用できます</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage; 