import React from 'react'
import heart from "../../assets/icons/heart_r.png";
import cmmnt from "../../assets/icons/alert.png";
import quest from "../../assets/icons/question_g.png";
import thum from "../../assets/icons/no_thum.png";
import { Link } from 'react-router-dom';



const MainToday = ({props}) => {

  function todayCont() {
    return (props.map((today) => (
      <li className="today_con" key={today.id}>
        <Link to={`/today/${today.id}`}>
          {(today.thum)
          ? <div className="thum"><img src={`http://localhost:3000/files/${today.thum}`} alt={`${today.title}`} /></div>
          : <div className="thum"><img src={`${thum}`} alt={`${today.title}`} /></div>}
          <h4>{today.title}</h4>
          <p className="cont">{today.content}</p>
          <ul className="today_emotion">
            {(today.answered === true)
              ? <li><span className="isAnsweredTrue">답변완료</span></li>
              : <li><span className="isAnsweredFalse">답변중</span></li>}
            <li className='quest'><img src={quest} alt="quest" /><span>{today.quest}</span></li>
            <li className='likes'><img src={heart} alt="heart" /><span>{today.likes}</span></li>
            <li className='cmmnt'><img src={cmmnt} alt="cmmnt" /><span>{today.comments}</span></li>
          </ul>
        </Link>
      </li>
    )))
  }



  return (
    <>
      <div className="today">
        <div className="title_area">
          <h2>빈즈투데이</h2>
          <Link to="/today" className="more">빈즈투데이 더보기</Link>
        </div>
        <div>
          <ul>
            {todayCont()}
          </ul>
        </div>
      </div>
    </>
  )
}

export default MainToday