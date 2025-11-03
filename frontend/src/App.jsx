import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MovieList from './components/MovieList';
import MovieCard from './components/MovieCard';
import FeaturedMovies from './components/FeaturedMovies';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import AdminDashboard from './components/AdminDashboard';
import Login from './components/Login';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider } from './context/AuthContext';
import './styles/App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/admin" element={
              <ProtectedRoute requireAdmin={true}>
                <AdminDashboard />
              </ProtectedRoute>
            } />
            <Route path="/" element={
              <>
                <div className="hero-wrapper">
                  <div className="container">
                    <Navbar />
                    <Hero />
                  </div>
                </div>
                <div className="container">
                  <FeaturedMovies />
                </div>
              </>
            } />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;