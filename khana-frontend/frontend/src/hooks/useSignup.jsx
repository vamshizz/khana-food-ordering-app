import { useNavigate } from "react-router-dom";
import { useState } from "react";
export  function useSignup(params) {
  const navigate = useNavigate();
  const[error,setError]=useState(null);
   const[isLoading,setLoading]=useState(null);
    const signup=async(email,password)=>{
      setLoading(true);
    setError(null);
      try {
        const res=await fetch("http://localhost:8072/api/users/signup",{
            method:'POST',
            headers:{'content-type':'application/json'},
            body:JSON.stringify({email,password})
          })
          const json = await res.json();  
          console.log(json);    
         
          if(!res.ok){
            console.log('Error occurred during signup:', json);
            setError(json.error);
          }
          if(res.ok){
            navigate("/login");
          }
        
      } catch (error) {
        console.error('An unexpected error occurred:', error);
      setError('An unexpected error occurred. Please try again.');
      }
          
    }
    return { signup,isLoading,error };
};
