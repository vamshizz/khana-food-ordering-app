import { useState } from "react";
import { useSignup } from "../hooks/useSignup";

export default function Login(params) {
  
  const{signup,isLoading,error}=useSignup()
    const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handlesubmit=async(e)=>{
    e.preventDefault();
    console.log("akkadddaa")
    await signup(email,password)

}

    return(
        <div  className="login">
            <form onSubmit={handlesubmit}>
 
    
    <div class="form-group row">
      
      <div class="col-sm-10">
        <input type="text" readonly="" class="form-control-plaintext" id="staticEmail" />
      </div>
    </div>
    <div class="form-group" onSubmit={handlesubmit}>
      <label for="exampleInputEmail1" class="form-label mt-4">Email address</label>
      <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email" value={email} onChange={(e) => setEmail(e.target.value)}/>
 
    </div>
    <div class="form-group">
      <label for="exampleInputPassword1" class="form-label mt-4">Password</label>
      <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password" autoComplete="off" value={password} onChange={(e) => setPassword(e.target.value)}/>

    </div>
    <button disabled= {isLoading} className="login-btn butt">Signup</button><br/>
   {error&&<div>{error}</div>}
      </form>
        </div>
    )
};
