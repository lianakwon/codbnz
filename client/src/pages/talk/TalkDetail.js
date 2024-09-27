import React, { useEffect, useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import "../../assets/chat.scss";
import "../../assets/chatDetail.scss";

import { componentDidMount } from "../../Axios";
import axios from "axios";
import authAxios from "../../interceptors";



const TalkDetail = ({ kind, props }) => {

  let { id } = useParams();

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { window.location.replace('/talk'); };

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState('');
  const [page, setPage] = useState('');

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => {
    if (kind === 'bnzTalk') setPage('talk');
    else if (kind === 'bnzTeam') setPage('team');
    componentDidMount().then(res => {
      if (res === undefined || res === null) { } else {
        setLoginUser(res); getLoginUser(res); getTalkList(res); getMessageList(res); getTitle();
      }
    }).catch(setLoginUser(''));
  }, [kind, props, id, page]);

  useEffect(() => {
    if (loginUser === undefined || loginUser === null || loginUser === '') { return; }
    else { setInterval(() => { getTalkList(loginUser); getMessageList(loginUser); }, 20000); }
  }, [kind, props, id, page]);

  const [talks, setTalks] = useState([]); // 톡
  const [messages, setMessages] = useState([]); // 메세지
  const [latestMessages, setLatestMessages] = useState({}); // 최신메세지
  const [cont, setCont] = useState([]); // 폼 제출용

  const [text, setText] = useState(); // 팀or톡
  const [title, setTitle] = useState(""); // 톡방 타이틀
  const [profile, setProfile] = useState({ userId: 0, nickname: '' }); // 로그인 유저

  // 로그인 유저 / 톡 / 챗

  const getLoginUser = async (loginUser) => {
    const res = (await authAxios.get(`http://localhost:8080/my/get_account/${loginUser}`)).data;
    setProfile({ userId: res.id, nickname: res.nickname });
  }

  const getTalkList = async (loginUser) => {
    try {
      let res;
      let formattedTalks;
      if (page === 'talk') {
        res = await authAxios.get(`http://localhost:8080/talk/${loginUser}`);
        formattedTalks = res.data.map(talk => ({
          talkid: talk.id,
          from: talk.from.nickname,
          to: talk.to.nickname
        }));
      } else if (page === 'team') {
        res = await authAxios.get(`http://localhost:8080/team/${loginUser}`);
        formattedTalks = res.data.map(team => ({
          talkid: team.id,
          from: team.team_title,
          to: team.team_intro
        }));
      }
      if (formattedTalks !== talks) setTalks(formattedTalks);
    } catch (err) { console.log(err); }
  }

  const getMessageList = async (loginUser) => {
    try {
      let res;
      let formattedMessages;
      if (page === 'talk') {
        res = await authAxios.get(`http://localhost:8080/talk/msg/${loginUser}`);
        if (res.data !== null && res.data !== undefined && res.data !== "") {
          formattedMessages = res.data.map(msg => ({
            talkid: msg.talk.id,
            talktitle: msg.talk.from.nickname + " - " + msg.talk.to.nickname,
            date: msg.date,
            time: msg.time,
            from: msg.from,
            cont: msg.cont
          }));
        }
      } else if (page === 'team') {
        res = await authAxios.get(`http://localhost:8080/team/msg/${loginUser}`);
        if (res.data !== null && res.data !== undefined && res.data !== "") {
          formattedMessages = res.data.map(msg => ({
            talkid: msg.team.id,
            talktitle: msg.team.team_title,
            date: msg.date,
            time: msg.time,
            from: msg.from,
            cont: msg.cont
          }));
        }
      }
      if (formattedMessages !== null && formattedMessages !== undefined && formattedMessages !== "") {
        const formattedMessages2 = formattedMessages.filter(msg => msg.talkid == id);
        if (formattedMessages2 !== messages) {
          setMessages(formattedMessages2);
          const latestMsgs = formattedMessages.reduce((acc, msg) => { if (!acc[msg.talkid] || new Date(msg.date + ' ' + msg.time) > new Date(acc[msg.talkid].date + ' ' + acc[msg.talkid].time)) acc[msg.talkid] = msg; return acc; }, {});
          setLatestMessages(latestMsgs);
          setTitle(latestMsgs[id].talktitle);
          console.log(latestMsgs[id].talktitle);
        }
      }
    } catch (err) { console.log(err); }
  }

  // 출력

  const getTitle = () => {
    try {
      if (page == 'team') setText(<h4 className='talk_title'>
        <span className="selected"><Link to={`/team`}>TEAM</Link></span>
        <span><Link to={`/talk`}>TALK</Link></span>
      </h4>);
      else if (page == 'talk') return setText(<h4 className='talk_title'>
        <span><Link to={`/team`}>TEAM</Link></span>
        <span className="selected"><Link to={`/talk`}>TALK</Link></span>
      </h4>);
    } catch (err) { console.log(err) }
  }

  function talkTag() {
    return (
      <>
        {(talks !== null && talks !== undefined)
          ? (<ul>{talks.map((talk, key) => {
            return (<li key={key}><Link to={`/${page}/${talk.talkid}`}>
              <div className="img_box" />
              <div className="txt_box">
                <h4>{talk.from} - {talk.to}</h4>
                {talkMsgsTag(talk.talkid)}
              </div>
            </Link></li>)
          })}</ul>)
          : <></>}
      </>
    );
  };

  const talkMsgsTag = (id) => {
    const message = latestMessages[id];
    return message ? <span>{message.cont}</span> : <span>아직 대화를 시작하지 않았어요. 지금 대화를 나눠 보세요!</span>;
  };

  const getLogin = () => {
    window.location.replace('/account/login');
  }

  // 폼

  const onChangeInput = (e) => { setCont(e.target.value); };

  const onSubmit = async (e) => {
    e.preventDefault();
    await authAxios.post(`http://localhost:8080/${page}/msg/${profile.userId}/${id}`, { cont: cont })
      .then(res => { console.log(res); window.location.reload(); document.querySelector('input[name="message"]').focus(); })
      .catch(err => { console.log(err); });
  }

  // 스크롤

  const messagesEndRef = useRef(null);

  const scrollToBottom = () => { messagesEndRef.current?.scrollIntoView(); };

  useEffect(() => { scrollToBottom(); }, [setProfile, messages, page]);

  // 멤버



  function msgsTag() {
    return (
      <div className="talk_list right" >
        <h4 className='talk_title'>{title}</h4>
        <div className="talk_detail_inner">
          {(messages.length < 1)
            ?
            <div>
              <p className="talk_date">2024.04.23</p>
              <p style={{ textAlign: 'center', margin: '40px 0', color: '#3339' }}>아직 대화를 시작하지 않았어요. 지금 대화를 나눠 보세요!</p>
            </div>
            :
            <div>
              <p className="talk_date">2024.04.23</p>
              <ul>{messages.map((msg, key) => {
                return ((msg.from.id === profile.userId)
                  ? (
                    <li key={key} className="mine">
                      <div className="txt_box">
                        <span className="time">{msg.time}</span>
                        <span className="cont">{msg.cont}</span>
                      </div>
                    </li>)
                  : (
                    <li key={key}>
                      <div className="img_box" />
                      <div className="txt_box">
                        <h4>{msg.from.nickname}</h4>
                        <span className="cont">{msg.cont}</span>
                        <span className="time">{msg.time}</span>
                      </div>
                    </li>))
              })}<div ref={messagesEndRef}></div></ul>
            </div>}
          <form onSubmit={onSubmit}>
            <div className="img_box" />
            <label htmlFor="message"><h4>{profile.nickname}</h4></label>
            <input name="message" value={cont} onChange={onChangeInput} placeholder="메세지를 입력하세요." />
            <button type="submit" />
          </form>
          <div className="detail_contents detail_black_cont"></div>
        </div>
      </div>
    );
  };

  return (
    <>
      {(loginUser === '')
        ? <div className="talk_cont">
          <div className="talk_list left" >
            {text}
            <div className="talk_list_inner">
              <p>지금 로그인하고 코드빈즈의 채팅을 이용해보세요!</p>
              <button type="button" onClick={getLogin}>로그인하러가기</button>
            </div>
            <div className="detail_contents detail_black_cont"></div>
          </div>
        </div>
        : <div className="talk_cont">
          <div className="talk_list left" >
            {text}
            <div className="talk_list_inner">
              {talkTag()}
              <div className="detail_contents detail_black_cont"></div>
            </div>
          </div>
          {msgsTag()}
        </div>
      }
    </>
  );
};

export default TalkDetail;
