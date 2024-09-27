import React, { useEffect, useState } from 'react'
import heart from "../../assets/icons/heart_r.png";
import quest from "../../assets/icons/question_g.png";
import { Link } from 'react-router-dom';



const TodayComment = ({ props }) => {

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
            <ul>{props.map((cmnte, key) => (
              <li className="today_con" key={key}>
                <Link to={`/today/${cmnte.id}`}>
                  <h4 style={{display:'inline', margin:'0 8px 0 0'}}>{cmnte.content}</h4>
                  <span>{cmnte.title}</span>
                  <ul className="today_emotion">
                    {(cmnte.answered === 'IN_PROGRESS')
                      ? <li><span className="isAnsweredTrue">{isAnswered(cmnte.answered)}</span></li>
                      : <li><span className="isAnsweredFalse">{isAnswered(cmnte.answered)}</span></li>}
                    <li className='quest'><img src={quest} alt="quest" /><span>{cmnte.quest}</span></li>
                    <li className='likes'><img src={heart} alt="heart" /><span>{cmnte.likes}</span></li>
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

export default TodayComment