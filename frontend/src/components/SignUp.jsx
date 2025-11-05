import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaFilm, FaUser, FaLock, FaEnvelope, FaExclamationCircle } from 'react-icons/fa';
import '../styles/Auth.css';

const SignUp = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const { register, loading } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const validateForm = () => {
        // Check if all fields are filled
        if (!formData.username.trim() || !formData.email.trim() || 
            !formData.password.trim() || !formData.confirmPassword.trim()) {
            setError('Please fill in all fields');
            return false;
        }

        // Validate username length
        if (formData.username.length < 3 || formData.username.length > 50) {
            setError('Username must be between 3 and 50 characters');
            return false;
        }

        // Validate email format
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(formData.email)) {
            setError('Please enter a valid email address');
            return false;
        }

        // Validate password length
        if (formData.password.length < 6) {
            setError('Password must be at least 6 characters long');
            return false;
        }

        // Check if passwords match
        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            return false;
        }

        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        // Validate form
        if (!validateForm()) {
            return;
        }

        // Attempt registration
        const result = await register(formData.username, formData.email, formData.password);
        
        if (result.success) {
            // Redirect to home page on success
            navigate('/');
        } else {
            setError(result.error);
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-container">
                <div className="auth-header">
                    <FaFilm className="auth-logo-icon" />
                    <h1>CineVerse</h1>
                    <p>Create your account</p>
                </div>

                {error && (
                    <div className="auth-error">
                        <FaExclamationCircle />
                        <span>{error}</span>
                    </div>
                )}

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="username">
                            <FaUser className="input-icon" />
                            Username
                        </label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            placeholder="Choose a username"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">
                            <FaEnvelope className="input-icon" />
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Enter your email"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">
                            <FaLock className="input-icon" />
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="Create a password"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">
                            <FaLock className="input-icon" />
                            Confirm Password
                        </label>
                        <input
                            type="password"
                            id="confirmPassword"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            placeholder="Confirm your password"
                            disabled={loading}
                        />
                    </div>

                    <button 
                        type="submit" 
                        className="auth-button"
                        disabled={loading}
                    >
                        {loading ? 'Creating account...' : 'Sign Up'}
                    </button>
                </form>

                <div className="auth-footer">
                    <p>
                        Already have an account? 
                        <Link to="/signin" className="auth-link"> Sign In</Link>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default SignUp;

