import { useSelector } from 'react-redux';
import { getTotalCartPrice, getTotalCartQuantity } from './cartslice';
import { getCart } from '../cart/cartslice';
import CartItem from './CartItem';
import { Link, useParams } from 'react-router-dom';
import BlastEffect from './BlastEffect';
import { useEffect } from 'react';

export default function OrderPage() {
   const { restaurantId } = useParams();
  const totalprice = useSelector(getTotalCartPrice);
  const totalquantity = useSelector(getTotalCartQuantity);
  const cart = useSelector((state) => state.cart.cart);
  useEffect(() => {
    console.log("Updated Cart in OrderPage:", cart);
  }, [cart]);

  

  return (
    <div className="items">
       <Link to={`/menu/${restaurantId}`}><h2>Main Menu</h2></Link>
      {cart && cart.length > 0 ? (
        <>
         
          <ul className="mt-3 divide-y divide-stone-200 border-b">
            {cart.map((item) => (
              <CartItem item={item} key={item.itemId} />
            ))}
          </ul>

          <h3>Price Details</h3>
          <div className="priceoverview">
            <div className="pricedetails">
              <h5>Price({totalquantity}) </h5>
              <h5>Delivery charges </h5>
              <h4>Total Price </h4>
            </div>
            <div className="price">
              <h5> {totalprice}</h5>
              <h5> 50</h5>
              <h4> {totalprice + 50}</h4>
            </div>
          </div>
          <BlastEffect />
        </>
      ) : (
        <h3>No items in cart</h3>
      )}
    </div>
  );
}
