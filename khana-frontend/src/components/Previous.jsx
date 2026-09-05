import React, { useEffect, useReducer } from 'react';
import { useSelector } from 'react-redux';

const initialState = {
  orders: [],
  sortCriteria: { field: 'orderDate', order: 'desc' },
};

const reducer = (state, action) => {
  switch (action.type) {
    case 'SET_ORDERS':
      return { ...state, orders: action.payload };
    case 'SET_SORT_CRITERIA':
      return { ...state, sortCriteria: action.payload };
    default:
      return state;
  }
};

const sortOrders = (orders, sortCriteria) => {
  return [...orders].sort((a, b) => {
    if (sortCriteria.field === 'orderDate') {
      return sortCriteria.order === 'asc'
        ? new Date(a.orderDate) - new Date(b.orderDate)
        : new Date(b.orderDate) - new Date(a.orderDate);
    } else if (sortCriteria.field === 'amount') {
      return sortCriteria.order === 'asc'
        ? a.amount - b.amount
        : b.amount - a.amount;
    }
    return 0;
  });
};

const Previous = () => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const user = useSelector((state) => state.user.isLoggedin);
  const token = useSelector((state) => state.user.user.token);

  useEffect(() => {
    if (user) {
      fetch("http://localhost:8072/api/orders/orderhistory", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((response) => response.json())
        .then((data) => {
          console.log("API response:", data);
          dispatch({ type: 'SET_ORDERS', payload: Array.isArray(data) ? data : [] });
        })
        .catch((error) => console.error("Error fetching orders:", error));
    }
  }, [user, token]);

  const handleSortChange = (event) => {
    const [field, order] = event.target.value.split(':');
    dispatch({ type: 'SET_SORT_CRITERIA', payload: { field, order } });
  };

  const sortedOrders = sortOrders(state.orders, state.sortCriteria);

  return (
    <div className='prev'>
      <h1>Order History:</h1>

      <label htmlFor="sort">Sort by: </label>
      <select id="sort" name="sort" onChange={handleSortChange}>
        <option value="orderDate:desc">Date: Newest First</option>
        <option value="orderDate:asc">Date: Oldest First</option>
        <option value="amount:asc">Price: Low to High</option>
        <option value="amount:desc">Price: High to Low</option>
      </select>

      <ul>
        {sortedOrders.map((order, index) => (
          <li key={index}>
            <h4>{new Date(order.orderDate).toLocaleString()}</h4>
            <ul>
              {order.items.map((item, itemIndex) => (
                <li key={itemIndex}>
                  <h5>{item.quantity} × {item.itemName}</h5>
                </li>
              ))}
            </ul>
            <h5>Total Cost: ₹{order.amount.toFixed(2)}</h5>
            <hr />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Previous;