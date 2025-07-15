import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import EstimatePage from '../EstimatePage';
import { loanProductApi, estimateApi } from '../../services/api';

// APIモック
jest.mock('../../services/api');
const mockedLoanProductApi = loanProductApi as jest.Mocked<typeof loanProductApi>;
const mockedEstimateApi = estimateApi as jest.Mocked<typeof estimateApi>;

// React Routerのコンテキストを提供するためのラッパー
const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  );
};

describe('EstimatePage', () => {
  const mockProducts = [
    {
      productId: 'PROD001',
      productName: '固定金利住宅ローン',
      productType: 'FIXED' as const,
      currentInterestRate: 1.5,
      minLoanAmount: 1000,
      maxLoanAmount: 5000,
      minLoanTerm: 10,
      maxLoanTerm: 35,
      repaymentMethod: 'EQUAL_PAYMENT' as const,
      isActive: true
    },
    {
      productId: 'PROD002',
      productName: '変動金利住宅ローン',
      productType: 'VARIABLE' as const,
      currentInterestRate: 0.8,
      minLoanAmount: 1000,
      maxLoanAmount: 5000,
      minLoanTerm: 10,
      maxLoanTerm: 35,
      repaymentMethod: 'EQUAL_PAYMENT' as const,
      isActive: true
    }
  ];

  const mockEstimate = {
    estimateId: 'EST001',
    productId: 'PROD001',
    productName: '固定金利住宅ローン',
    loanAmount: 3000,
    loanTerm: 30,
    interestRate: 1.5,
    monthlyPayment: 10350,
    totalPayment: 3726000,
    totalInterest: 726000
  };

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders estimate page heading', () => {
    renderWithRouter(<EstimatePage />);
    const heading = screen.getByText('住宅ローン見積もり');
    expect(heading).toBeInTheDocument();
  });

  test('renders subtitle', () => {
    renderWithRouter(<EstimatePage />);
    const subtitle = screen.getByText('お客様の条件に合わせて最適なローンをシミュレーションします');
    expect(subtitle).toBeInTheDocument();
  });

  test('loads and displays products on mount', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
      expect(screen.getByText('変動金利住宅ローン')).toBeInTheDocument();
    });

    expect(mockedLoanProductApi.getAll).toHaveBeenCalledTimes(1);
  });

  test('displays error when products fail to load', async () => {
    mockedLoanProductApi.getAll.mockRejectedValue(new Error('Failed to load products'));

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('商品情報の取得に失敗しました')).toBeInTheDocument();
    });
  });

  test('renders form elements', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByLabelText('ローン商品を選択してください')).toBeInTheDocument();
      expect(screen.getByLabelText('融資希望額（万円）')).toBeInTheDocument();
      expect(screen.getByLabelText('融資期間（年）')).toBeInTheDocument();
      expect(screen.getByText('見積もり計算')).toBeInTheDocument();
    });
  });

  test('submits form with valid data', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);
    mockedEstimateApi.calculate.mockResolvedValue(mockEstimate);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
    });

    // フォームに入力
    const productSelect = screen.getByLabelText('ローン商品を選択してください');
    const amountInput = screen.getByLabelText('融資希望額（万円）');
    const termSelect = screen.getByLabelText('融資期間（年）');

    fireEvent.change(productSelect, { target: { value: 'PROD001' } });
    fireEvent.change(amountInput, { target: { value: '3000' } });
    fireEvent.change(termSelect, { target: { value: '30' } });

    // フォーム送信
    const submitButton = screen.getByText('見積もり計算');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(mockedEstimateApi.calculate).toHaveBeenCalledWith({
        productId: 'PROD001',
        loanAmount: 3000,
        loanTerm: 30
      });
    });
  });

  test('displays error for invalid input', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
    });

    // 空のフォームで送信
    const submitButton = screen.getByText('見積もり計算');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('必要な情報が入力されていません')).toBeInTheDocument();
    });
  });

  test('displays error when calculation fails', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);
    mockedEstimateApi.calculate.mockRejectedValue(new Error('Calculation failed'));

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
    });

    // フォームに入力
    const productSelect = screen.getByLabelText('ローン商品を選択してください');
    const amountInput = screen.getByLabelText('融資希望額（万円）');
    const termSelect = screen.getByLabelText('融資期間（年）');

    fireEvent.change(productSelect, { target: { value: 'PROD001' } });
    fireEvent.change(amountInput, { target: { value: '3000' } });
    fireEvent.change(termSelect, { target: { value: '30' } });

    // フォーム送信
    const submitButton = screen.getByText('見積もり計算');
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(screen.getByText('見積もり計算に失敗しました')).toBeInTheDocument();
    });
  });

  test('displays product information', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('利用可能なローン商品')).toBeInTheDocument();
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
      expect(screen.getByText('変動金利住宅ローン')).toBeInTheDocument();
      expect(screen.getByText('商品タイプ:')).toBeInTheDocument();
      expect(screen.getByText('現在金利:')).toBeInTheDocument();
      expect(screen.getByText('融資額範囲:')).toBeInTheDocument();
      expect(screen.getByText('融資期間:')).toBeInTheDocument();
    });
  });

  test('displays correct product type labels', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利')).toBeInTheDocument();
      expect(screen.getByText('変動金利')).toBeInTheDocument();
    });
  });

  test('displays product details correctly', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('1.5%')).toBeInTheDocument();
      expect(screen.getByText('0.8%')).toBeInTheDocument();
      expect(screen.getByText('1000万円 ～ 5000万円')).toBeInTheDocument();
      expect(screen.getByText('10年 ～ 35年')).toBeInTheDocument();
    });
  });

  test('renders back link', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      const backLink = screen.getByText('← トップページに戻る');
      expect(backLink).toBeInTheDocument();
      expect(backLink.closest('a')).toHaveAttribute('href', '/');
    });
  });

  test('shows loading state during calculation', async () => {
    mockedLoanProductApi.getAll.mockResolvedValue(mockProducts);
    mockedEstimateApi.calculate.mockImplementation(() => 
      new Promise(resolve => setTimeout(() => resolve(mockEstimate), 100))
    );

    renderWithRouter(<EstimatePage />);

    await waitFor(() => {
      expect(screen.getByText('固定金利住宅ローン')).toBeInTheDocument();
    });

    // フォームに入力
    const productSelect = screen.getByLabelText('ローン商品を選択してください');
    const amountInput = screen.getByLabelText('融資希望額（万円）');
    const termSelect = screen.getByLabelText('融資期間（年）');

    fireEvent.change(productSelect, { target: { value: 'PROD001' } });
    fireEvent.change(amountInput, { target: { value: '3000' } });
    fireEvent.change(termSelect, { target: { value: '30' } });

    // フォーム送信
    const submitButton = screen.getByText('見積もり計算');
    fireEvent.click(submitButton);

    // ローディング状態を確認
    expect(screen.getByText('計算中...')).toBeInTheDocument();
  });
}); 