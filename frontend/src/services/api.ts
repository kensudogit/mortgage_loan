import axios from 'axios';
import { LoanProduct, LoanEstimate, LoanApplication, EstimateRequest, ApiResponse } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/mortgage-loan';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ローン商品関連
export const loanProductApi = {
  getAll: async (): Promise<LoanProduct[]> => {
    const response = await api.get<ApiResponse<LoanProduct[]>>('/api/loan/products');
    return response.data.data || [];
  },

  getById: async (productId: string): Promise<LoanProduct | null> => {
    const response = await api.get<ApiResponse<LoanProduct>>(`/api/loan/products/${productId}`);
    return response.data.data || null;
  },
};

// 見積もり関連
export const estimateApi = {
  calculate: async (request: EstimateRequest): Promise<LoanEstimate> => {
    const response = await api.post<ApiResponse<LoanEstimate>>('/api/loan/estimate/calculate', request);
    if (!response.data.success) {
      throw new Error(response.data.message || '見積もり計算に失敗しました');
    }
    return response.data.data!;
  },

  getById: async (estimateId: string): Promise<LoanEstimate | null> => {
    const response = await api.get<ApiResponse<LoanEstimate>>(`/api/loan/estimate/${estimateId}`);
    return response.data.data || null;
  },
};

// 申し込み関連
export const applicationApi = {
  submit: async (application: LoanApplication): Promise<LoanApplication> => {
    const response = await api.post<ApiResponse<LoanApplication>>('/api/loan/application/submit', application);
    if (!response.data.success) {
      throw new Error(response.data.message || '申し込みに失敗しました');
    }
    return response.data.data!;
  },

  getById: async (applicationId: string): Promise<LoanApplication | null> => {
    const response = await api.get<ApiResponse<LoanApplication>>(`/api/loan/application/${applicationId}`);
    return response.data.data || null;
  },

  getByCustomerId: async (customerId: string): Promise<LoanApplication[]> => {
    const response = await api.get<ApiResponse<LoanApplication[]>>(`/api/loan/application/customer/${customerId}`);
    return response.data.data || [];
  },
};

export default api; 