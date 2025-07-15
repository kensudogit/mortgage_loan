import { loanProductApi, estimateApi, applicationApi } from '../api';
import axios from 'axios';

// axiosモック
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('API Services', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('loanProductApi', () => {
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
      }
    ];

    test('getAll returns products successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockProducts
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await loanProductApi.getAll();

      expect(result).toEqual(mockProducts);
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/loan/products');
    });

    test('getAll returns empty array when no data', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: null
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await loanProductApi.getAll();

      expect(result).toEqual([]);
    });

    test('getById returns product successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockProducts[0]
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await loanProductApi.getById('PROD001');

      expect(result).toEqual(mockProducts[0]);
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/loan/products/PROD001');
    });

    test('getById returns null when product not found', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: null
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await loanProductApi.getById('INVALID');

      expect(result).toBeNull();
    });
  });

  describe('estimateApi', () => {
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

    test('calculate returns estimate successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockEstimate
        }
      };
      mockedAxios.post.mockResolvedValue(mockResponse);

      const request = {
        productId: 'PROD001',
        loanAmount: 3000,
        loanTerm: 30
      };

      const result = await estimateApi.calculate(request);

      expect(result).toEqual(mockEstimate);
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/loan/estimate/calculate', request);
    });

    test('calculate throws error when API returns failure', async () => {
      const mockResponse = {
        data: {
          success: false,
          message: 'Calculation failed'
        }
      };
      mockedAxios.post.mockResolvedValue(mockResponse);

      const request = {
        productId: 'PROD001',
        loanAmount: 3000,
        loanTerm: 30
      };

      await expect(estimateApi.calculate(request)).rejects.toThrow('Calculation failed');
    });

    test('getById returns estimate successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockEstimate
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await estimateApi.getById('EST001');

      expect(result).toEqual(mockEstimate);
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/loan/estimate/EST001');
    });

    test('getById returns null when estimate not found', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: null
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await estimateApi.getById('INVALID');

      expect(result).toBeNull();
    });
  });

  describe('applicationApi', () => {
    const mockApplication = {
      applicationId: 'APP001',
      customerName: '田中太郎',
      customerEmail: 'tanaka@example.com',
      customerPhone: '090-1234-5678',
      productId: 'PROD001',
      productName: '固定金利住宅ローン',
      loanAmount: 3000,
      loanTerm: 30,
      interestRate: 1.5,
      monthlyPayment: 10350,
      propertyAddress: '東京都渋谷区1-1-1',
      propertyType: 'DETACHED' as const,
      propertyValue: 4000,
      employmentType: 'SALARIED' as const,
      annualIncome: 600,
      bankAccountNumber: '1234567890',
      bankName: 'テスト銀行',
      branchName: '渋谷支店',
      applicationStatus: 'PENDING' as const
    };

    test('submit returns application successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockApplication
        }
      };
      mockedAxios.post.mockResolvedValue(mockResponse);

      const result = await applicationApi.submit(mockApplication);

      expect(result).toEqual(mockApplication);
      expect(mockedAxios.post).toHaveBeenCalledWith('/api/loan/application/submit', mockApplication);
    });

    test('submit throws error when API returns failure', async () => {
      const mockResponse = {
        data: {
          success: false,
          message: 'Submission failed'
        }
      };
      mockedAxios.post.mockResolvedValue(mockResponse);

      await expect(applicationApi.submit(mockApplication)).rejects.toThrow('Submission failed');
    });

    test('getById returns application successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: mockApplication
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await applicationApi.getById('APP001');

      expect(result).toEqual(mockApplication);
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/loan/application/APP001');
    });

    test('getById returns null when application not found', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: null
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await applicationApi.getById('INVALID');

      expect(result).toBeNull();
    });

    test('getByCustomerId returns applications successfully', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: [mockApplication]
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await applicationApi.getByCustomerId('CUST001');

      expect(result).toEqual([mockApplication]);
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/loan/application/customer/CUST001');
    });

    test('getByCustomerId returns empty array when no applications', async () => {
      const mockResponse = {
        data: {
          success: true,
          data: null
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await applicationApi.getByCustomerId('CUST001');

      expect(result).toEqual([]);
    });
  });

  describe('Error handling', () => {
    test('handles network errors', async () => {
      mockedAxios.get.mockRejectedValue(new Error('Network error'));

      await expect(loanProductApi.getAll()).rejects.toThrow('Network error');
    });

    test('handles malformed response', async () => {
      const mockResponse = {
        data: {
          success: true
          // data field missing
        }
      };
      mockedAxios.get.mockResolvedValue(mockResponse);

      const result = await loanProductApi.getAll();

      expect(result).toEqual([]);
    });
  });
}); 