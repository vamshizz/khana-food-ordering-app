import { createSlice } from "@reduxjs/toolkit";
 
const initialState={
  isLoggedin:false,
    user:null
}

const userslice=createSlice({
    name:'user',
    initialState,
    reducers:{
          username(state,action){
            state.isLoggedin=true;
            state.user = action.payload ;
             
          }
          ,
          logout(state,action){
            state.isLoggedin=false;
            state.user = null;
            
          }
    },
})

export const{
    username,
    logout
}=userslice.actions;
 
export default userslice.reducer;
 
 
