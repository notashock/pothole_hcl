import React, { useState, createContext, useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import axios from 'axios';

// 1. Setup a simple Auth Context to manage state
const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

// --- PLACEHOLDER DASHBOARD COMPONENTS ---
const CitizenDashboard = () => (
  <div className="p-6 bg-white rounded-lg shadow-md mt-4">
    <h2 className="text-2xl font-bold text-blue-600">Citizen Dashboard</h2>
    <p className="mt-2 text-gray-600">Map UI and Report Form will go here.</p>
  </div>
);

const OfficerDashboard = () => (
  <div className="p-6 bg-white rounded-lg shadow-md mt-4">
    <h2 className="text-2xl font-bold text-green-600">Municipal Officer Dashboard</h2>
    <p className="mt-2 text-gray-600">Complaint approval table will go here.</p>
  </div>
);

const SupervisorDashboard = () => (
  <div className="p-6 bg-white rounded-lg shadow-md mt-4">
    <h2 className="text-2xl font-bold text-purple-600">Supervisor Dashboard</h2>
    <p className="mt-2 text-gray-600">Engineer assignment interface will go here.</p>
  </div>
);

const EngineerDashboard = () => (
  <div className="p-6 bg-white rounded-lg shadow-md mt-4">
    <h2 className="text-2xl font-bold text-orange-600">Engineer Dashboard</h2>
    <p className="mt-2 text-gray-600">Work order updates and repair logs will go here.</p>
  </div>
);

// --- LOGIN COMPONENT ---
const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // NOTE: Update this URL to match your Spring Boot port if it's not 8080
      const response = await axios.post('http://localhost:8080/auth/users/login', {
        email,
        password,
      });
      
      const { token, role } = response.data;
      login(token, role);
    } catch (err) {
      setError('Invalid credentials or server is down. Check console.');
      console.error(err);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-[80vh]">
      <form onSubmit={handleLogin} className="bg-white p-8 rounded-xl shadow-lg w-96 border border-gray-200">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">System Login</h2>
        {error && <p className="text-red-500 text-sm mb-4 text-center">{error}</p>}
        
        <div className="mb-4">
          <label className="block text-gray-700 text-sm font-bold mb-2">Email</label>
          <input 
            type="email" 
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required 
          />
        </div>
        
        <div className="mb-6">
          <label className="block text-gray-700 text-sm font-bold mb-2">Password</label>
          <input 
            type="password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            required 
          />
        </div>
        
        <button type="submit" className="w-full bg-blue-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-blue-700 transition">
          Sign In
        </button>
      </form>
    </div>
  );
};

// --- PROTECTED ROUTE WRAPPER ---
const ProtectedRoute = ({ children, allowedRole }) => {
  const { user } = useAuth();
  
  if (!user) return <Navigate to="/login" />;
  if (allowedRole && user.role !== allowedRole) return <Navigate to="/unauthorized" />;
  
  return children;
};

// --- MAIN APP COMPONENT ---
export default function App() {
  const [user, setUser] = useState(null); // { token: '', role: '' }

  const login = (token, role) => {
    setUser({ token, role });
    // In a real app, you would save the token to localStorage here
    // localStorage.setItem('token', token);
  };

  const logout = () => {
    setUser(null);
    // localStorage.removeItem('token');
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      <Router>
        <div className="min-h-screen bg-gray-50 font-sans text-gray-900">
          
          {/* Simple Navigation Bar */}
          <nav className="bg-slate-800 text-white p-4 shadow-md flex justify-between items-center">
            <h1 className="text-xl font-bold tracking-wide">PotholeTracker Pro</h1>
            <div>
              {user ? (
                <div className="flex items-center gap-4">
                  <span className="text-sm bg-slate-700 px-3 py-1 rounded-full border border-slate-600">
                    Role: {user.role}
                  </span>
                  <button onClick={logout} className="text-sm bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg transition font-medium">
                    Logout
                  </button>
                </div>
              ) : (
                <Link to="/login" className="text-sm bg-blue-500 hover:bg-blue-600 px-4 py-2 rounded-lg transition font-medium">
                  Login
                </Link>
              )}
            </div>
          </nav>

          {/* Main Content Area */}
          <main className="container mx-auto p-4">
            <Routes>
              <Route path="/" element={<Navigate to="/login" />} />
              <Route path="/login" element={user ? <Navigate to={`/${user.role.toLowerCase()}`} /> : <Login />} />
              
              {/* Dashboards mapped to roles */}
              <Route path="/citizen" element={
                <ProtectedRoute allowedRole="CITIZEN"><CitizenDashboard /></ProtectedRoute>
              } />
              <Route path="/municipal_officer" element={
                <ProtectedRoute allowedRole="MUNICIPAL_OFFICER"><OfficerDashboard /></ProtectedRoute>
              } />
              <Route path="/supervisor" element={
                <ProtectedRoute allowedRole="SUPERVISOR"><SupervisorDashboard /></ProtectedRoute>
              } />
              <Route path="/engineer" element={
                <ProtectedRoute allowedRole="ENGINEER"><EngineerDashboard /></ProtectedRoute>
              } />

              <Route path="/unauthorized" element={
                <div className="text-center mt-20 text-red-600 text-2xl font-bold">403 - Unauthorized Access</div>
              } />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthContext.Provider>
  );
}