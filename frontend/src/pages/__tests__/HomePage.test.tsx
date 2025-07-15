import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import HomePage from '../HomePage';

// React Routerのコンテキストを提供するためのラッパー
const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

describe('HomePage', () => {
  test('renders main heading', () => {
    renderWithRouter(<HomePage />);
    const heading = screen.getByText('住宅ローンシステム');
    expect(heading).toBeInTheDocument();
  });

  test('renders subtitle', () => {
    renderWithRouter(<HomePage />);
    const subtitle = screen.getByText('安心・安全な住宅ローンサービスを提供いたします');
    expect(subtitle).toBeInTheDocument();
  });

  test('renders loan estimate card', () => {
    renderWithRouter(<HomePage />);
    const estimateCard = screen.getByText('🏠 ローン見積もり');
    expect(estimateCard).toBeInTheDocument();
  });

  test('renders loan application card', () => {
    renderWithRouter(<HomePage />);
    const applicationCard = screen.getByText('📋 ローン申し込み');
    expect(applicationCard).toBeInTheDocument();
  });

  test('renders simulation card', () => {
    renderWithRouter(<HomePage />);
    const simulationCard = screen.getByText('📊 返済シミュレーション');
    expect(simulationCard).toBeInTheDocument();
  });

  test('renders feature section', () => {
    renderWithRouter(<HomePage />);
    const featureHeading = screen.getByText('システムの特徴');
    expect(featureHeading).toBeInTheDocument();
  });

  test('renders all feature items', () => {
    renderWithRouter(<HomePage />);
    
    const accuracyFeature = screen.getByText('🎯 正確な計算');
    const speedFeature = screen.getByText('⚡ 迅速な処理');
    const securityFeature = screen.getByText('🔒 セキュリティ');
    const usabilityFeature = screen.getByText('📱 使いやすさ');
    
    expect(accuracyFeature).toBeInTheDocument();
    expect(speedFeature).toBeInTheDocument();
    expect(securityFeature).toBeInTheDocument();
    expect(usabilityFeature).toBeInTheDocument();
  });

  test('renders estimate link', () => {
    renderWithRouter(<HomePage />);
    const estimateLink = screen.getByText('見積もりを始める');
    expect(estimateLink).toBeInTheDocument();
    expect(estimateLink.closest('a')).toHaveAttribute('href', '/estimate');
  });

  test('renders application link', () => {
    renderWithRouter(<HomePage />);
    const applicationLink = screen.getByText('申し込み手続き');
    expect(applicationLink).toBeInTheDocument();
    expect(applicationLink.closest('a')).toHaveAttribute('href', '/application');
  });

  test('renders simulation link', () => {
    renderWithRouter(<HomePage />);
    const simulationLink = screen.getByText('シミュレーション');
    expect(simulationLink).toBeInTheDocument();
    expect(simulationLink.closest('a')).toHaveAttribute('href', '/estimate');
  });

  test('renders feature descriptions', () => {
    renderWithRouter(<HomePage />);
    
    const accuracyDesc = screen.getByText(/最新の金利情報を基に、正確な返済額を計算します/);
    const speedDesc = screen.getByText(/自動審査システムにより、迅速な審査結果をお届けします/);
    const securityDesc = screen.getByText(/お客様の個人情報は厳重に管理し、安全に取り扱います/);
    const usabilityDesc = screen.getByText(/直感的な操作で、誰でも簡単に利用できます/);
    
    expect(accuracyDesc).toBeInTheDocument();
    expect(speedDesc).toBeInTheDocument();
    expect(securityDesc).toBeInTheDocument();
    expect(usabilityDesc).toBeInTheDocument();
  });

  test('renders card descriptions', () => {
    renderWithRouter(<HomePage />);
    
    const estimateDesc = screen.getByText(/お客様の条件に合わせて、最適な住宅ローンの見積もりを計算いたします/);
    const applicationDesc = screen.getByText(/見積もり結果を基に、簡単な手続きで住宅ローンの申し込みができます/);
    const simulationDesc = screen.getByText(/様々な返済プランをシミュレーションして、お客様に最適な返済方法を見つけることができます/);
    
    expect(estimateDesc).toBeInTheDocument();
    expect(applicationDesc).toBeInTheDocument();
    expect(simulationDesc).toBeInTheDocument();
  });
}); 