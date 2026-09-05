import { useEffect } from "react";
import UpdateQuantity from "./UpdateQuantity"
import { deleteItem,getCurrentQuantityById  } from "./cartslice"
import { useDispatch, useSelector, } from 'react-redux'
import { Link } from "react-router-dom";
export default function CartItem({item}) {
    const dispatch=useDispatch();
    const cart = useSelector((state) => state.cart.cart);
     const currentquantity=useSelector(getCurrentQuantityById(item.itemId));
    console.log(currentquantity)
  
    
    
    const isItemInCart = cart.some((cartItem) => cartItem.itemid === item.itemid);
    useEffect(() => {
      console.log("Updated Cart in OrderPage:", cart);
      if(currentquantity===0&&isItemInCart){
        dispatch(deleteItem(item.itemid))
      }
    }, [item,dispatch]);
    
   
 
    return(
         
            
        <div className="item">
  <div className="cartdetails">
 
<h4>{item.quantity}&times; {item.itemname}  </h4>
<div><h5>{item.unitPrice}💲</h5> </div>
 

</div>
 <div className="pic">
 <UpdateQuantity itemId={item.itemId} currentquantity= {currentquantity}/>
 <h5>{item.quantity*item.unitPrice}</h5>
 </div>

 
</div>

 
        
        
    )
};
