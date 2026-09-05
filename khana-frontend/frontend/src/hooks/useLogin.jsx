import { useState } from "react";
import { useDispatch  } from 'react-redux';
import {username} from '../components/features/user/userslice'
import { useNavigate } from "react-router-dom";
export const useLogin=()=>{
    const navigate = useNavigate();
   const[error,setError]=useState(null);
   const[isLoading,setLoading]=useState(null);
   const dispatch = useDispatch();
   
   const login=async(email,password)=>{
     
    setLoading(true);
    setError(null);
    const res= await fetch("http://localhost:8072/api/users/login",{
       
        method:'POST',
        headers:{'content-type':'application/json'},
        body:JSON.stringify({email,password})


    })
    console.log(res);
   console.log("backedn request poindhi")
    const json= await res.json();
    if (!res.ok) {
        setLoading(false);
        setError(json.error);
        console.log('Error occurred during login:', json.error); // Log the error message
      
       
      }
      
    if(res.ok){
        console.log('Login successful:', json);
        setLoading(false);
 
localStorage.setItem('user', JSON.stringify(json));

 

// Update the auth context
dispatch(username(json));
navigate("/");
 

    }
   }
   return{login,isLoading,error}
}