import Confetti from 'react-confetti';
 import verify from '../cart/verify.png'
 
export default function Orderaccepted(params) {
    return(
        <div className='order-accepted'>
             <span className='donepic'>
        <img src={verify} alt="Verification Icon" style={{
            width: '100px', // Set the width to your desired size
            height: '100px', // Set the height to your desired size
          }}/>
      </span>
            <h1>Order accepted </h1>
            <h5>Delevery boy will asssign shortly</h5>
            <Confetti
  numberOfPieces={1000} // Number of confetti pieces
  gravity={0.1} // Gravity strength (0 to 1)
  recycle={false} // Whether to recycle the particles
  width={window.innerWidth} // Width of the confetti container
  height={window.innerHeight} // Height of the confetti container
/>
        </div>
    )
};
