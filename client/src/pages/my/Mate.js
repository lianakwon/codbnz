import React from 'react'
import heart from "../../assets/icons/heart_r.png";
import cmmnt from "../../assets/icons/alert.png";
import quest from "../../assets/icons/question_g.png";
import { Link } from 'react-router-dom';



const Mate = ({ props }) => {

  function mateCont() {
    return (
      (props.length === 0)
        ? <></>
        : <div className="mate">
          <div>
            <ul>{props.map((mate, key) => (
              <li className="mate_con" key={key}>
                <Link to={`/mate/${mate.id}`}>
                  <h4 style={{display:'inline', margin:'0 8px 0 0'}}>{mate.title}</h4><span>{mate.content}</span>
                  <ul className="mate_emotion">
                    <li className='quest'><img src={quest} alt="quest" /><span>{mate.views}</span></li>
                    <li className='likes'><img src={heart} alt="heart" /><span>{mate.likes}</span></li>
                    {/* <li><img src={cmmnt} alt="cmmnt" /><span>{mate.cmmnt}</span></li> */}
                  </ul>
                </Link>
              </li>
            ))}</ul>
            <h3>@bnzMate</h3>
          </div>
        </div>)
  }



  return (
    <>
      {mateCont()}
    </>
  )
}

export default Mate