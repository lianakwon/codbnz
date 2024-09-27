import React from 'react'
import heart from "../../../assets/icons/heart_r.png";
import cmmnt from "../../../assets/icons/alert.png";
import quest from "../../../assets/icons/question_g.png";
import { Link } from 'react-router-dom';

const Mate = () => {

  const mates = [
    { id: 1, title: "1 토요일에 용산에서 함께 열공하실 분!", author: "잠못드는콩", quest: 5, heart: 6, cmmnt: 7 },
    { id: 2, title: "2 토요일에 용산에서 함께 열공하실 분!", author: "잠못드는콩", quest: 5, heart: 6, cmmnt: 7 },
    { id: 3, title: "3 토요일에 용산에서 함께 열공하실 분!", author: "잠못드는콩", quest: 5, heart: 6, cmmnt: 7 },
    { id: 4, title: "4 토요일에 용산에서 함께 열공하실 분!", author: "잠못드는콩", quest: 5, heart: 6, cmmnt: 7 },
  ];

  function mateCont() {
    return (mates.map((mate, key) => (
      <li key={key} className="mate_con">
        <Link to={`/mate/${mate.id}`}>
          <h4>{mate.title}</h4>
          <ul className="mate_emotion">
            <li><img src={quest} alt="quest" /><span>{mate.quest}</span></li>
            <li><img src={heart} alt="heart" /><span>{mate.heart}</span></li>
            <li><img src={cmmnt} alt="cmmnt" /><span>{mate.cmmnt}</span></li>
          </ul>
        </Link>
      </li>
    )))
  }



  return (
    <>
      <div className="mate">
        <div>
          <ul>
            {mateCont()}
          </ul>
          <h3>@bnzMate</h3>
        </div>
      </div>
    </>
  )
}

export default Mate