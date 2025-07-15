import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { loanProductApi, estimateApi } from '../services/api';
import { LoanProduct, EstimateRequest } from '../types';
import './EstimatePage.css';

const EstimatePage: React.FC = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState<LoanProduct[]>([]);
  const [selectedProductId, setSelectedProductId] = useState<string>('');
  const [loanAmount, setLoanAmount] = useState<string>('');
  const [loanTerm, setLoanTerm] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const productList = await loanProductApi.getAll();
      setProducts(productList);
    } catch (err) {
      setError('商品情報の取得に失敗しました');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!selectedProductId || !loanAmount || !loanTerm) {
      setError('必要な情報が入力されていません');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const request: EstimateRequest = {
        productId: selectedProductId,
        loanAmount: parseFloat(loanAmount),
        loanTerm: parseInt(loanTerm)
      };

      const estimate = await estimateApi.calculate(request);
      
      // 結果をセッションストレージに保存
      sessionStorage.setItem('estimate', JSON.stringify(estimate));
      
      navigate('/estimate/result');
    } catch (err) {
      setError(err instanceof Error ? err.message : '見積もり計算に失敗しました');
    } finally {
      setLoading(false);
    }
  };

  const getProductTypeLabel = (type: string) => {
    switch (type) {
      case 'FIXED': return '固定金利';
      case 'VARIABLE': return '変動金利';
      case 'MIXED': return 'ミックス';
      default: return type;
    }
  };

  return (
    <div className="container">
      <div className="header">
        <h1>住宅ローン見積もり</h1>
        <p>お客様の条件に合わせて最適なローンをシミュレーションします</p>
      </div>

      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <div className="form-card">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="selectedProductId">ローン商品を選択してください</label>
            <select
              id="selectedProductId"
              value={selectedProductId}
              onChange={(e) => setSelectedProductId(e.target.value)}
              className="form-control"
              required
            >
              <option value="">商品を選択してください</option>
              {products.map(product => (
                <option key={product.productId} value={product.productId}>
                  {product.productName}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="loanAmount">融資希望額（万円）</label>
            <input
              type="number"
              id="loanAmount"
              value={loanAmount}
              onChange={(e) => setLoanAmount(e.target.value)}
              className="form-control"
              placeholder="例: 3000"
              min="100"
              max="10000"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="loanTerm">融資期間（年）</label>
            <select
              id="loanTerm"
              value={loanTerm}
              onChange={(e) => setLoanTerm(e.target.value)}
              className="form-control"
              required
            >
              <option value="">期間を選択してください</option>
              <option value="10">10年</option>
              <option value="15">15年</option>
              <option value="20">20年</option>
              <option value="25">25年</option>
              <option value="30">30年</option>
              <option value="35">35年</option>
            </select>
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? '計算中...' : '見積もり計算'}
          </button>
        </form>
      </div>

      {products.length > 0 && (
        <div className="form-card">
          <h3>利用可能なローン商品</h3>
          {products.map(product => (
            <div key={product.productId} className="product-info">
              <h3>{product.productName}</h3>
              <div className="product-details">
                <div className="detail-item">
                  <strong>商品タイプ:</strong><br />
                  <span>{getProductTypeLabel(product.productType)}</span>
                </div>
                <div className="detail-item">
                  <strong>現在金利:</strong><br />
                  <span>{product.currentInterestRate}%</span>
                </div>
                <div className="detail-item">
                  <strong>融資額範囲:</strong><br />
                  <span>{product.minLoanAmount}万円 ～ {product.maxLoanAmount}万円</span>
                </div>
                <div className="detail-item">
                  <strong>融資期間:</strong><br />
                  <span>{product.minLoanTerm}年 ～ {product.maxLoanTerm}年</span>
                </div>
              </div>
              {product.description && (
                <p className="product-description">{product.description}</p>
              )}
            </div>
          ))}
        </div>
      )}

      <div className="back-link">
        <a href="/">← トップページに戻る</a>
      </div>
    </div>
  );
};

export default EstimatePage; 