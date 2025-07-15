import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import EstimatePage from './pages/EstimatePage';
import EstimateResultPage from './pages/EstimateResultPage';
import ApplicationPage from './pages/ApplicationPage';
import ApplicationCompletePage from './pages/ApplicationCompletePage';
import ErrorPage from './pages/ErrorPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/estimate" element={<EstimatePage />} />
          <Route path="/estimate/result" element={<EstimateResultPage />} />
          <Route path="/application" element={<ApplicationPage />} />
          <Route path="/application/complete" element={<ApplicationCompletePage />} />
          <Route path="/error" element={<ErrorPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App; 