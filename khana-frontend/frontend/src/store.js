import { configureStore } from "@reduxjs/toolkit";
import cartReducer from "./components/features/cart/cartslice";
import userReducer from "./components/features/user/userslice"
const preloadedState = {
  user: {
    isLoggedin: !!JSON.parse(localStorage.getItem('user')),
    user: JSON.parse(localStorage.getItem('user')) || null,
  },
};
 
 

const store=configureStore({
    reducer:{
        cart:cartReducer,
        user:userReducer
    } ,
    preloadedState
})
export default store;