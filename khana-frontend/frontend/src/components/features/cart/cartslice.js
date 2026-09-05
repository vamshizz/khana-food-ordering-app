import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  cart: [],
};

const cartSlice = createSlice({
  name: 'cart',
  initialState,
  reducers: {
    addItem(state, action) {
        console.log('Adding item:', action.payload);
      state.cart = [...state.cart, action.payload];
      console.log('Updated cart:', state.cart);
    },
    deleteItem(state, action) {
       state.cart = state.cart.filter((item) => item.itemid !== action.payload)
    },
    increaseItemQuantity(state, action) {
      console.log("hello vamshi slice");
      console.log(action.payload);
      const item = state.cart.find((item) => item.itemId=== action.payload);
console.log(item.quantity);
      item.quantity++;
      item.totalPrice = item.quantity * item.unitPrice;
    },
    decreaseItemQuantity(state, action) {
      const item=state.cart.find((item)=>item.itemId===action.payload)
      
      item.quantity--;
      item.totalprice=item.quantity*item.totalprice;
    },
    clearCart: (state) => {
      return  initialState;
    }
  },
});

export const {
  addItem,
  deleteItem,
  increaseItemQuantity,
  decreaseItemQuantity,
  clearCart,
} = cartSlice.actions;

export default cartSlice.reducer;
export const getCart = (state) => state.cart?.cart;

export const getTotalCartQuantity = (state) =>
  state.cart.cart.reduce((sum, item) => sum + item.quantity, 0);

export const getTotalCartPrice = (state) =>
  state.cart.cart.reduce((sum, item) => sum +  item.quantity*item.unitPrice, 0);

export const getCurrentQuantityById = (id) => (state) =>
 (state.cart.cart.find((cart) => cart.itemId === id)?.quantity ?? 0);

