import { useSelector } from 'react-redux';
import { Link,useLocation } from 'react-router-dom';
 
import { getTotalCartPrice,getTotalCartQuantity } from './cartslice';  
export default function CartOverview({restaurantId }) {
  
     const totalCartQuantity = useSelector(getTotalCartQuantity);
    const cart = useSelector((state) => state.cart.cart);
console.log("Cart in CartOverview:", cart);
   
    return(
        <div>
        { totalCartQuantity>0?<div className="cart-info">
        <h3>{totalCartQuantity} items added  <Link to={`/cart/${restaurantId }`}>Open cart </Link></h3>
        </div>:null}
        
       
        </div>
    )
};
