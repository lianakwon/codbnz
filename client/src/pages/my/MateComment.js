import React from 'react'
import heart from "../../assets/icons/heart_r.png";
import quest from "../../assets/icons/question_g.png";
import { Link } from 'react-router-dom';



const MateComment = ({ props }) => {

  function mateCont() {
    return (
      (props.length === 0)
        ? <></>
        : <div className="mate">
          <div>
            <ul>{props.map((cmnte, key) => (
              <li className="mate_con" key={key}>
                <Link to={`/mate/${cmnte.id}`}>
                  <h4 style={{display:'inline', margin:'0 8px 0 0'}}>{cmnte.content}</h4>
                  <span>{cmnte.title}</span>
                  <ul className="mate_emotion">
                    {/* <li className='quest'><img src={quest} alt="quest" /><span>{cmnte.views}</span></li> */}
                    <li className='likes'><img src={heart} alt="heart" /><span>{cmnte.likes}</span></li>
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

export default MateComment