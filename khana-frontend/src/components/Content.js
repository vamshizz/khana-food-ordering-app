import React from 'react';
import Button from './Button'; 
import UpdateQuantity from '../components/features/cart/UpdateQuantity' 
import { useDispatch, useSelector } from 'react-redux';
import { addItem, getCurrentQuantityById, deleteItem } from '../components/features/cart/cartslice';

export default function Content({ item }) {
  const dispatch = useDispatch();
  const cart = useSelector((state) => state.cart.cart);

  console.log(item)
  console.log(cart);
  const currentquantity = useSelector(getCurrentQuantityById(item.itemId));
 

  const isItemInCart = cart.some((cartItem) => cartItem.itemId === item.itemId);
  console.log(isItemInCart)
  if (currentquantity === 0 && isItemInCart) {
    dispatch(deleteItem(item.itemid))
  }

  function handleAddToCart() {

    const newItem = {
      itemId: item.itemId,
      itemname: item.itemName,
      quantity: 1,
      unitPrice: item.unitPrice,
      totalPrice: item.unitPrice * 1,
    };

    dispatch(addItem(newItem));
  }
  console.log(currentquantity)
  return (

    <div className="item">
      <div className="item-details">
        <h4>{item.itemName}</h4>
        <h5>{item.unitPrice}💲</h5>
        <p>⭐⭐⭐⭐⭐</p>
      </div>
      <div className="pic">
        <img src={"http://localhost:8080/images/" + item.itemImage} alt={item.itemName} width="150px" />
        {isItemInCart && currentquantity > 0 ? (
          <UpdateQuantity itemId={item.itemId} currentquantity={currentquantity} />
        ) : (
          <Button onClick={handleAddToCart}>Add to Cart</Button>
        )}
      </div>

    </div>


  );
}
