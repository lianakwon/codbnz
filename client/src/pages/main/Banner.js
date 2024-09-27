import React from 'react'
import { Link } from "react-router-dom"
import { Carousel } from 'react-responsive-carousel'
import "react-responsive-carousel/lib/styles/carousel.min.css";
import person from "../../assets/images/person.png";
import cheer from "../../assets/images/cheer.png";
import calendar from "../../assets/images/calendar.png";

const MainBanner = () => {

  const onChange = () => { }
  const onClickItem = () => { }
  const onClickThumb = () => { }



  return (
    <>
      <div className="banner">
        <Carousel
          axis='horizontal'
          emulateTouch={true}
          swipeable={true}
          controlArrow={false}
          autoPlay={false}
          showArrows={false}
          infiniteLoop={false}
          showThumbs={false}
          onChange={onChange}
          onClickItem={onClickItem}
          onClickThumb={onClickThumb}>
          <div className="banner_1">
            <Link to="/mate">
              <p className="thumb"><img src={person} alt='person' /></p>
              <p className="title">코딩하는 콩들, 여기여기 붙어라!</p>
              <p className="decs">
                친구와 함께 으쌰으쌰 공부하고 싶은 콩?<br />
                같이 공부할 친구들을 빈즈메이트에서 모집해보세요.</p>
              <p className="logos">codbnz</p></Link></div>
          <div className="banner_2">
            <Link to="/today">
              <p className="thumb"><img src={cheer} alt='cheer' style={{ bottom: 0 }} /></p>
              <p className="title">혹시, 이거 아는 빈즈?</p>
              <p className="decs">
                이거 왜 에러 떠?ㅠㅠ 나 좀 도와줄 빈즈!<br />
                자유롭게 질문과 답변을 주고받으며 함께 성장해요.</p>
              <p className="logos">codbnz</p></Link></div>
          <div className="banner_3">
            <Link to="/plan">
              <p className="thumb"><img src={calendar} alt='calendar' style={{ bottom: -72 }} /></p>
              <p className="title">빈즈, 오늘은 이거 하는 날이야.</p>
              <p className="decs">
                일정 관리가 어렵나요? 이제는 캘린더도 함께 공유해요!<br />
                팀원들과 함께 공유하는 편리한 빈즈 플래너.</p>
              <p className="logos">codbnz</p></Link></div>
        </Carousel>
      </div>
    </>
  )
}

export default MainBanner