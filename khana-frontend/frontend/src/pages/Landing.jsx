import Authentic from "./Authentic"
import boy3 from "../images/delevieryboy4.avif"
import road from "../images/road.jpeg"
import loc from "../images/delboy5.avif"
export default function Landing(){
    return(

        <div className="lund">
              <div className="del"> 
           <img src={boy3} className="boygads"></img>
           <img src={loc} className="roadd"></img>
           </div>
           <div className="auth">
            <Authentic/>
         </div>
          

        </div>
    )
    
        
        
        
        
    
}