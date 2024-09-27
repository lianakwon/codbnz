import React from 'react'
import heart from "../../../assets/icons/heart_r.png";
import cmmnt from "../../../assets/icons/alert.png";
import quest from "../../../assets/icons/question_g.png";
import thum from "../../../assets/icons/no_thum.png";
import { Link } from 'react-router-dom';



const Today = () => {

  const todays = [
    { id: 1, thum: "../../assets/icons/no_thum.png", title: "1 List<T> 컴파일 에러가 납니다. 도와주세요", cont: "List<T> 컴파일 에러가 납니다. 도와주세요 List<T> 컴파일 에러가 납니다. 도와주세요", author: "콩", isAnswered: true, quest: 5, heart: 6, cmmnt: 7 },
    { id: 2, thum: "../../assets/icons/no_thum.png", title: "2 List<T> 컴파일 에러가 납니다. 도와주세요", cont: "List<T> 컴파일 에러가 납니다. 도와주세요 으악", author: "콩", isAnswered: false, quest: 5, heart: 6, cmmnt: 7 },
    { id: 3, thum: "../../assets/icons/no_thum.png", title: "3 List<T> 컴파일 에러가 납니다. 도와주세요", cont: "List<T> 컴파일 에러가 납니다. 도와주세요 List<T> 컴파일 에러가 납니다. 도와주세요", author: "콩", isAnswered: true, quest: 5, heart: 6, cmmnt: 7 },
    { id: 4, thum: "../../assets/icons/no_thum.png", title: "4 List<T> 컴파일 에러가 납니다. 도와주세요", cont: "List<T> 컴파일 에러가 납니다. 도와주세요 List<T> 컴파일 에러가 납니다. 도와주세요", author: "콩", isAnswered: true, quest: 5, heart: 6, cmmnt: 7 },
  ];

  function todayCont() {
    return (todays.map((today) => (
      <li className="today_con" key={today.id}>
        <Link to={`/today/${today.id}`}>
          <div className="thum"><img src={thum} alt="thum" /></div>
          <h4>{today.title}</h4>
          <ul className="today_emotion">
            {(today.isAnswered === true)
              ? <li><span className="isAnsweredTrue">답변완료</span></li>
              : <li><span className="isAnsweredFalse">답변중</span></li>}
            <li><img src={quest} alt="quest" /><span>{today.quest}</span></li>
            <li><img src={heart} alt="heart" /><span>{today.heart}</span></li>
            <li><img src={cmmnt} alt="cmmnt" /><span>{today.cmmnt}</span></li>
          </ul>
        </Link>
      </li>
    )))
  }



  return (
    <>
      <div className="today">
        <div>
          <ul>
            {todayCont()}
          </ul>
          <h3>@bnzToday</h3>
        </div>
      </div>
    </>
  )
}

export default Today