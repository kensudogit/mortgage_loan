export interface LoanProduct {
  productId: string;
  productName: string;
  productType: 'FIXED' | 'VARIABLE' | 'MIXED';
  minInterestRate?: number;
  maxInterestRate?: number;
  currentInterestRate: number;
  minLoanAmount: number;
  maxLoanAmount: number;
  minLoanTerm: number;
  maxLoanTerm: number;
  repaymentMethod: 'EQUAL_PAYMENT' | 'EQUAL_PRINCIPAL';
  description?: string;
  isActive: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface LoanEstimate {
  estimateId?: string;
  productId: string;
  productName: string;
  loanAmount: number;
  loanTerm: number;
  interestRate: number;
  monthlyPayment: number;
  totalPayment?: number;
  totalInterest?: number;
  repaymentMethod?: string;
  estimatedAt?: string;
  customerId?: string;
}

export interface LoanApplication {
  applicationId?: string;
  customerId?: string;
  customerName: string;
  customerEmail: string;
  customerPhone: string;
  productId: string;
  productName: string;
  loanAmount: number;
  loanTerm: number;
  interestRate?: number;
  monthlyPayment?: number;
  applicationStatus?: 'PENDING' | 'APPROVED' | 'REJECTED' | 'PROCESSING';
  propertyAddress: string;
  propertyType: 'DETACHED' | 'APARTMENT' | 'CONDO';
  propertyValue: number;
  employmentType: 'SALARIED' | 'SELF_EMPLOYED' | 'BUSINESS_OWNER';
  annualIncome: number;
  bankAccountNumber: string;
  bankName: string;
  branchName: string;
  applicationDate?: string;
  approvalDate?: string;
  approvedBy?: string;
  rejectionReason?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface EstimateRequest {
  productId: string;
  loanAmount: number;
  loanTerm: number;
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
  errors?: string[];
} 