// src/components/AuthDebug.jsx
// Temporary debug component to check authentication status
import React from 'react';
import { useAuth } from '../context/AuthContext';

const AuthDebug = () => {
  const { user, token, isAuthenticated } = useAuth();
  
  const checkLocalStorage = () => {
    const storedToken = localStorage.getItem('token');
    const storedUser = localStorage.getItem('user');
    
    console.group('🔍 Auth Debug Info');
    console.log('Is Authenticated:', isAuthenticated);
    console.log('User from context:', user);
    console.log('Token from context:', token ? token.substring(0, 20) + '...' : 'No token');
    console.log('Token from localStorage:', storedToken ? storedToken.substring(0, 20) + '...' : 'No token');
    console.log('User from localStorage:', storedUser);
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      console.log('Parsed user:', parsedUser);
      console.log('User roles:', parsedUser.roles);
      console.log('Has ROLE_ADMIN:', parsedUser.roles?.includes('ROLE_ADMIN'));
    }
    console.groupEnd();
  };

  React.useEffect(() => {
    checkLocalStorage();
  }, []);

  return (
    <div style={{
      position: 'fixed',
      bottom: 20,
      right: 20,
      background: '#1e1e1e',
      border: '2px solid #ffd700',
      borderRadius: 8,
      padding: 20,
      color: '#fff',
      zIndex: 9999,
      maxWidth: 400,
      fontSize: 12,
      fontFamily: 'monospace'
    }}>
      <h4 style={{ margin: '0 0 10px 0', color: '#ffd700' }}>🔍 Auth Debug</h4>
      <div><strong>Authenticated:</strong> {isAuthenticated ? '✅ Yes' : '❌ No'}</div>
      <div><strong>Username:</strong> {user?.username || 'N/A'}</div>
      <div><strong>Email:</strong> {user?.email || 'N/A'}</div>
      <div><strong>Roles:</strong> {user?.roles ? user.roles.join(', ') : 'N/A'}</div>
      <div><strong>Has ROLE_ADMIN:</strong> {user?.roles?.includes('ROLE_ADMIN') ? '✅ Yes' : '❌ No'}</div>
      <div><strong>Token exists:</strong> {token ? '✅ Yes' : '❌ No'}</div>
      <button 
        onClick={checkLocalStorage}
        style={{
          marginTop: 10,
          background: '#ffd700',
          border: 'none',
          padding: '5px 10px',
          borderRadius: 4,
          cursor: 'pointer',
          fontWeight: 'bold'
        }}
      >
        Check Console
      </button>
    </div>
  );
};

export default AuthDebug;

