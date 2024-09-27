import React from 'react'
import heart from "../../assets/icons/heart_r.png";
import cmmnt from "../../assets/icons/alert.png";
import quest from "../../assets/icons/question_g.png";
import thum from "../../assets/icons/no_thum.png";
import { Link } from 'react-router-dom';



const Today = ({ props }) => {

  const isAnswered = (text) => {
    switch (text) {
      case 'IN_PROGRESS': return '문제해결!';
      case 'COMPLETED': return '궁금해요';
      case 'NOT_APPLICABLE': return '궁금해요';
      default: return ' ';
    }
  };


  function todayCont() {
    return (
      (props.length === 0)
        ? <></>
        : <div className="today">
          <div>
            <ul>{props.map((today, key) => (
              <li className="today_con" key={key}>
                <Link to={`/today/${today.id}`}>
                  <div className="thum"><img src={thum} alt="thum" /></div>
                  <h4 style={{display:'inline', margin:'0 8px 0 0'}}>{today.title}</h4><span>{today.content}</span>
                  <ul className="today_emotion">
                    {(today.answered === 'IN_PROGRESS')
                      ? <li><span className="isAnsweredTrue">{isAnswered(today.answered)}</span></li>
                      : <li><span className="isAnsweredFalse">{isAnswered(today.answered)}</span></li>}
                    <li className='quest'><img src={quest} alt="quest" /><span>{today.quest}</span></li>
                    <li className='likes'><img src={heart} alt="heart" /><span>{today.likes}</span></li>
                    {/* <li><img src={cmmnt} alt="cmmnt" /><span>{today.cmmnt}</span></li> */}
                  </ul>
                </Link>
              </li>
            ))}</ul>
            <h3>@bnzToday</h3>
          </div>
        </div>)
  }



  return (
    <>
      {todayCont()}
    </>
  )
}

export default Today