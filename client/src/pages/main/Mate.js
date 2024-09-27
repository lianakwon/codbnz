import React from 'react'
import heart from "../../assets/icons/heart_r.png";
import cmmnt from "../../assets/icons/alert.png";
import { Link } from 'react-router-dom';

const MainMate = ({props}) => {

  function mateCont() {
    return (props.map((mate, index) => (
      <li className="mate_con" key={mate.id}>
        <Link to={`/mate/${mate.id}`}>
          <h4>{mate.title}</h4>
          <p>{mate.content}</p>
          <p>@{mate.author}</p>
          <ul className="mate_emotion">
            <li className='likes'><img src={heart} alt="heart" /><span>{mate.likes}</span></li>
            <li className='cmmnt'><img src={cmmnt} alt="cmmnt" /><span>{mate.views}</span></li>
          </ul>
          <ul className="tagWrap">{mateTags(index)}</ul>
        </Link>
      </li>
    )))
  }

  function mateTags(index) {
    const tag = props[index].tags;
    console.log(tag);
    return (<>
      <li className="goal"><span>{tag.goal}</span></li>
      <li className="spce"><span>{tag.space}</span></li>
      <li className="area">{tag.locas.map((item, index) => <span key={index}>{item}</span>)}</li>
      <li className="part">{tag.parts.map((item, index) => <span key={index}>{item}</span>)}</li>
      <li className="lang">{tag.langs.map((item, index) => <span key={index}>{item}</span>)}</li>
    </>);
  }



  return (
    <>
      <div className="mate">
        <div className="title_area">
          <h2>빈즈메이트</h2>
          <Link to="/mate" className="more">빈즈메이트 더보기</Link>
        </div>
        <div>
          <ul>
            {mateCont()}
          </ul>
        </div>
      </div>
    </>
  )
}

export default MainMate