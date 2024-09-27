import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Mate from '../Mate'
import Today from '../Today'
import MateComment from '../MateComment'
import TodayComment from '../TodayComment'
import axios from 'axios'
import { componentDidMount } from '../../../Axios'

const Detail = () => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { navigate(-1); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState("");

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => { componentDidMount().then(res => { if (res === undefined) { alert('로그인 해주세요.'); navigate('/account/login'); }; loadPage(); setLoginUser(res); getLikeMs(res); getLikeCMs(res); getLikeTs(res); getQuests(res); getLikeTMs(res); }).catch(setLoginUser("")); }, []);

  function loadPage() { document.querySelector('input[id="tab1"]').click(); }

  const [mates, setMates] = useState([]);
  const [commentMs, setCommentMs] = useState([]);
  const [todays, setTodays] = useState([]);
  const [commentTs, setCommentTs] = useState([]);
  const [quests, setQuests] = useState([]);

  // 메이트 좋아요
  async function getLikeMs(loginUser) {
    if (loginUser === undefined) { alert('로그인 해주세요.'); navigate('/account/login'); }
    try {
      const res = (await axios.get(`http://localhost:8080/my/likes_m/${loginUser}`));
      setMates(res.data.map(mate => ({
        id: mate.id,
        title: mate.title,
        content: mate.content,
        author: mate.author.nickname,
        create_date: mate.create_date,
        update_date: mate.update_date,
        hits: mate.hits,
        likes: mate.likes
      })));
    } catch (err) { console.log(err); }
    console.log(mates);
  };

  // 메이트 좋아요 댓글
  async function getLikeCMs(loginUser) {
    try {
      const res = (await axios.get(`http://localhost:8080/my/likes_comments_m/${loginUser}`));
      console.log(res);
      setCommentMs(res.data.map(comment => ({
        id: comment.mate.id,
        content: comment.content,
        writer: comment.writer.nickname,
        title: comment.mate.title,
        author: comment.mate.author.nickname,
        create_date: comment.create_date,
        update_date: comment.update_date,
        likes: comment.likes
      })));
    } catch (err) { console.log(err); }
    // console.log(commentMs);
  };

  // 투데이 좋아요
  async function getLikeTs(loginUser) {
    try {
      const res = (await axios.get(`http://localhost:8080/my/likes_t/${loginUser}`));
      console.log(res);
      setTodays(res.data.map(today => ({
        id: today.todayId,
        title: today.title,
        content: today.content,
        author: today.writerName,
        answered: today.answerStatus,
        views: today.viewCount,
        likes: today.likeCount,
        quest: today.questionCount,
        comments: today.commentCount,
      })));
    } catch (err) { console.log(err); }
    // console.log(todays);
  };

  // 투데이 좋아요 댓글
  async function getLikeTMs(loginUser) {
    try {
      const res = (await axios.get(`http://localhost:8080/my/likes_comments_t/${loginUser}`));
      console.log(res);
      setCommentTs(res.data.map(today => ({
        id: today.today_id,
        content: today.content,
        writer: today.commentWriterName,
        title: today.today_title,
        author: today.author,
        create_date: today.createdDate,
        update_date: today.modifiedDate,
        answered: today.answered,
        views: today.view_count,
        likes: today.like_count,
        quest: today.quest_count
      })));
    } catch (err) { console.log(err); }
    // console.log(commentTs);
  };

  // 투데이 궁금해요
  async function getQuests(loginUser) {
    try {
      const res = (await axios.get(`http://localhost:8080/my/quests_t/${loginUser}`));
      console.log(res);
      setQuests(res.data.map(today => ({
        id: today.todayId,
        title: today.title,
        content: today.content,
        author: today.writerName,
        answered: today.answerStatus,
        views: today.viewCount,
        likes: today.likeCount,
        quest: today.questionCount,
        comments: today.commentCount,
      })));
    } catch (err) { console.log(err); }
    // console.log(quests);
  };    



  return (
    <>
      <div id='my' className="inner_m detail">
        <div>
          <div id="tabs">
            <h2 onClick={clickTitle} className='link_back'>좋아요 | 궁금해요</h2>
            <input type="radio" id="tab1" name="tab" /><label htmlFor="tab1">좋아요 게시글</label>
            <input type="radio" id="tab2" name="tab" /><label htmlFor="tab2">좋아요 댓글</label>
            <input type="radio" id="tab3" name="tab" /><label htmlFor="tab3">궁금해요 게시글</label>

            <div className="like board tab tab1">
              <Mate props={mates} />
              <Today props={todays} /></div>

            <div className="like board tab tab2">
              <MateComment props={commentMs} />
              <TodayComment props={commentTs} /></div>

            <div className="qust board tab tab3">
              <Today props={quests} /></div>
          </div>
        </div>
      </div>
    </>
  )
}

export default Detail