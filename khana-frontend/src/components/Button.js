import React from 'react';

export default function Button( { onClick }) {
     
return(
    <div className='wrapper'> <span className='button' onClick={onClick}>
    Add to Cart
  </span></div>
)
};
