import React, { useEffect, useState } from 'react';
import Content from './components/Content'; // Import the Content component
import { useParams } from "react-router-dom";
import { useSelector } from 'react-redux';
 import CartOverview from './components/features/cart/CartOverview';
export default function Menu() {
  const token=useSelector((state)=>state.user.user.token)
  const { restaurantId } = useParams();
  const [menuItems, setMenuItems] = useState([]);
  useEffect(() => {
 
    console.log(restaurantId)
    if (restaurantId) {
      
      fetch(`http://localhost:8072/api/orders/food/restaurant/${restaurantId}`, {
        method: "GET",
        headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",  
        },
    })
        .then((res) => res.json())
        .then((data) => setMenuItems(data.menuItemList))
        .catch((error) => console.error("Error fetching menu:", error));
    }
  }, [restaurantId]);

  console.log(restaurantId )

  useEffect(() => {
    console.log(menuItems)
  },[menuItems])
 
  function handleSortChange(e){
const x=e.target.value;
  if(x==="totalprice:asc"){
    const sortorder=[...menuItems].sort((a,b)=>a.unitPrice-b.unitPrice)
    setMenuItems(sortorder);
  }
  else{
    const sortorder=[...menuItems].sort((a,b)=>b.unitPrice-a.unitPrice)
    setMenuItems(sortorder);
  }
  }
 console.log(menuItems)
  return (
    <div>
       
    <div className='items'>
        {menuItems.map((item) => (
        <Content item={item} key={item.itemid} />
       
      ))}
     <select id="sort" name="sort" onChange={handleSortChange}>
       <option value="totalprice:asc">Price: Low to High</option>
       <option value="totalprice:desc">Price: High to Low</option>
       </select>
    </div>
    <CartOverview restaurantId={restaurantId}/>
    </div>
     
  );
}
