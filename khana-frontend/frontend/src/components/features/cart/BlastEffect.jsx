import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from "react-router-dom";
import { useCallback, useState ,useRef} from 'react';  // ← added useState
import { getTotalCartPrice, getTotalCartQuantity, clearCart } from './cartslice';

export default function BlastEffect() {
  const [loading, setLoading] = useState(false);
  const[idmKey, setidmKey] = useState(true);  // ← state declared

  const totalCartPrice = useSelector(getTotalCartPrice);
  const totalquantity = useSelector(getTotalCartQuantity);
  const token = useSelector((state) => state.user.user.token);
  const userEmail = useSelector((state) => state.user.user.email);
  const orderItems = useSelector((state) => state.cart.cart);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const idempotencyKeyRef = useRef(null);

  const handlePaymentSuccess = (response, orderId) => {
    fetch("http://localhost:8072/api/payments/verify", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        paymentId: response.razorpay_payment_id,
        orderId: response.razorpay_order_id,
        signature: response.razorpay_signature,
        PlacedOrderId: orderId,
      })
    })
      .then(res => {
        if (res.ok) return res.json();
        alert("Payment Verification Failed!");
      })
      .then(data => {
        if (data?.sucess) {   // ← fixed typo: sucess → success
          dispatch(clearCart());
          // Prepare for a completely new order
           idempotencyKeyRef.current = null;
          navigate("/orderaccepted");

           
        } else {
          alert("Payment Verification Failed!");
        }
      })
      .catch(err => console.error("Error:", err))
      .finally(() => setLoading(false));  // ← reset loading after verify completes
  };

  const handleClick = useCallback(async () => {
    if (loading) return;        // ← block duplicate clicks
    setLoading(true);  

    if (!idempotencyKeyRef.current) {
  idempotencyKeyRef.current = crypto.randomUUID();
}

const idempotencyKey = idempotencyKeyRef.current;
   

    
    
    console.log(idempotencyKey);         // ← disable button immediately

    try {
      const res = await fetch("http://localhost:8072/api/orders/food/createorder", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
          "Idempotency-Key": idempotencyKey
        },
        body: JSON.stringify({
          userEmail: userEmail,
          items: orderItems
        }),
      });

      if (!res.ok) {
        alert("Failed to place order. Please try again.");
        console.log("Error creating order",res);
        setLoading(false);
        setidmKey(false) ;   // ← reset if order creation fails
        return;
      }

      const data = await res.json();
      const { orderId, paymentDetails } = data;

      if (!window.Razorpay) {
        console.error("Razorpay SDK not loaded.");
        setLoading(false);      // ← reset if SDK missing
        return;
      }

      const options = {
        key: "rzp_test_icxG7hFAniopGY",
        amount: paymentDetails.amount,
        currency: paymentDetails.currency,
        name: "Food App",
        description: "Order Payment",
        order_id: paymentDetails.orderId,
        handler: async function (response) {
          handlePaymentSuccess(response, orderId);
        },
        modal: {
          ondismiss: () => setLoading(false),   // ← reset if user closes popup
        },
        prefill: {
          name: "John Doe",
          email: "john@example.com",
          contact: "9999999999",
        },
        theme: { color: "#3399cc" },
      };

      const razor = new window.Razorpay(options);
      razor.open();

    } catch (error) {
      console.error("Error:", error);
      setLoading(false);        // ← reset on any unexpected error
    }
  }, [token, userEmail, orderItems, totalCartPrice, loading]);  // ← added loading to deps

  return (
    <div className="orderpage">
      <button
        onClick={handleClick}
        disabled={loading}                          // ← button disabled while loading
        style={{ opacity: loading ? 0.6 : 1, cursor: loading ? 'not-allowed' : 'pointer' }}
      >
        {loading ? "Processing..." : "Place Order"}  
      </button>
    </div>
  );
}