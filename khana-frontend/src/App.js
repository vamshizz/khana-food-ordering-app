import './App.css';
import Menu from './Menu';
import Navbar from './components/Navbar';
import OrderPage from './components/features/cart/OrderPage';
import { Routes, Route, Navigate,useLocation  } from 'react-router-dom';
import Home from './components/Home';
import React, { useEffect } from 'react';
import CartOverview from './components/features/cart/CartOverview';
import Orderaccepted from './components/features/cart/Orderaccepted';
import Signup from './pages/Signup';
import Login from './pages/Login';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import Previous from './components/Previous';
import Landing from './pages/Landing';
function App() {
  const user = useSelector((state) => state.user.isLoggedin);
  const navigate = useNavigate();
 const location= useLocation();
  useEffect(() => {
    console.log('User:', user);
     
    if (!user  ) {
      console.log('Redirecting to /login');
      navigate('/first');
    }
  }, [user, navigate, location.pathname]);

  

  return (
    <div className="App">
      <Navbar />
       
      <Routes>
        <Route path="/" element={user ? <Home /> : <Navigate to="/first" />} />
        <Route path="/cart/:restaurantId" element={user ? <OrderPage /> : <Navigate to="/first" />} />
        <Route path="/menu/:restaurantId" element={user ? <Menu /> : <Navigate to="/first" />} />
        <Route path="/orderaccepted" element={user ? <Orderaccepted /> : <Navigate to="/first" />} />
        <Route path="/history" element={user ? <Previous /> : <Navigate to="/first" />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/first" element={<Landing/>}/>
      </Routes>
       
    </div>
  );
}

export default App;
