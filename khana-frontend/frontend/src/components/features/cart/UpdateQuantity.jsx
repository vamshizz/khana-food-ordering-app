import React from 'react';
import { useDispatch  } from 'react-redux';
import {   increaseItemQuantity, decreaseItemQuantity } from './cartslice';

export default function UpdateQuantity({ itemId,currentquantity}) {
  const dispatch = useDispatch();
  
   console.log("hello vamshi");

  return (
    <div className="wrapper">
      <span className="button" onClick={() => dispatch(decreaseItemQuantity(itemId))}>-</span>
      <span>{currentquantity}</span>
      <span className="button" onClick={() => dispatch(increaseItemQuantity(itemId))}>+</span>
    </div>
  );
}
