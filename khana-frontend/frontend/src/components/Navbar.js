import { useSelector, useDispatch } from 'react-redux';
import { logout } from './features/user/userslice';
import { useNavigate } from "react-router-dom";
import { clearCart } from './features/cart/cartslice';
import Dropdown from 'react-bootstrap/Dropdown';
import { useState } from 'react';

export default function Navbar() {
  const reduxState = useSelector((state) => state?.user?.user);
  console.log(reduxState);
  const email = reduxState?.email;
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };
  
  const navigate = useNavigate();
  
  const user = useSelector((state) => state.user.isLoggedin);
  const cart = useSelector((state) => state.cart);
  console.log(cart);
  
  const dispatch = useDispatch();

  const handleLogout = () => {
    localStorage.removeItem('user');
    dispatch(logout());
    dispatch(clearCart()); 
    navigate("/login");
  };

  const inputStyle = {
    color: 'black',
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg bg-primary navbar" data-bs-theme="dark">
        <div className="container-fluid">
          <a className="navbar-brand" href="/">KHANA</a>

          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="navbarColor01">
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <li className="nav-item dropdown">

                  <a className="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span className="iconloc"><i className="fa-solid fa-location-dot"></i></span> Hyderabad
                  </a>

                  <div className="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a className="dropdown-item" href="#">Action</a>
                    <a className="dropdown-item" href="#">Another action</a>
                    <a className="dropdown-item" href="#">Something else here</a>
                    <div className="dropdown-divider"></div>
                    <a className="dropdown-item" href="#">Separated link</a>
                  </div>
                </li>
              </li>

              <li className="nav-item">
                <form className="d-flex">
                  <input style={inputStyle} className="form-control me-sm-2 placeholder-black" type="search" placeholder="Search places" />
                  <button className="btn btn-secondary my-2 my-sm-0 search-button" type="submit">Search</button>
                </form>
              </li>
            </ul>

            {user ? (
              <div className="vamshi">
                <div className="hamburger" onClick={toggleMenu}>
                  <div className="bar"></div>
                  <div className="bar"></div>
                  <div className="bar"></div>
                </div>
                {isOpen && (
                  <div className="vk">
                    <a>{email}</a>
                    <a href="/history">Previous Orders</a>
                    <a href="#" onClick={handleLogout}>
                      Logout
                    </a>
                  </div>
                )}
              </div>
            ) : (
              <ul className="navbar-nav">
                
              </ul>
            )}
          </div>
        </div>
      </nav>
    </div>
  );
}
