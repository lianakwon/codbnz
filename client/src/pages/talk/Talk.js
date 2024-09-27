import { Link, useNavigate } from "react-router-dom";
import "../../assets/chat.scss";
import { useEffect, useState } from "react";

import { componentDidMount } from "../../Axios";
import axios from "axios";
import authAxios from "../../interceptors";



const Talk = ({ kind }) => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { window.location.replace('/'); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState('');
  const [page, setPage] = useState('');

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => {
    if (kind === `bnzTalk`) setPage('talk');
    else if (kind === `bnzTeam`) setPage('team');
    componentDidMount().then(res => {
      if (res === undefined || res === null) { getTitle(); } else {
        setLoginUser(res); getLoginUser(res); getTalkList(res); getMessageList(res); getTitle();
      }
    }).catch(setLoginUser(''));
  }, [kind, page]);

  const [talks, setTalks] = useState([]);
  const [messages, setMessages] = useState([]);
  const [latestMessages, setLatestMessages] = useState({});
  const [text, setText] = useState(null);
  const [add, setAdd] = useState(null);
  const [profile, setProfile] = useState({ id: 0, nickname: '' });

  // 로그인 유저 정보

  const getLoginUser = async (loginUser) => {
    const res = (await authAxios.get(`http://localhost:8080/my/get_account/${loginUser}`)).data;
    setProfile({ id: res.id, nickname: res.nickname });
  }

  const getTalkList = async (loginUser) => {
    try {
      let res;
      let formattedTalks;
      if (page === 'talk') {
        res = await authAxios.get(`http://localhost:8080/talk/${loginUser}`);
        console.log(res)
        formattedTalks = res.data.map(talk => ({
          talkid: talk.id,
          from: talk.from.nickname,
          to: talk.to.nickname
        }));
      } else if (page === 'team') {
        res = await authAxios.get(`http://localhost:8080/team/${loginUser}`);
        console.log(res)
        formattedTalks = res.data.map(team => ({
          talkid: team.id,
          from: team.team_title,
          to: team.team_intro
        }));
      }
      if (formattedTalks !== talks) setTalks(formattedTalks);
    } catch (err) { console.log(err); }
    console.log(talks);
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
            date: msg.date,
            time: msg.time,
            from: msg.from,
            cont: msg.cont
          }));
        }
      }
      if (formattedMessages !== null && formattedMessages !== undefined && formattedMessages !== "") {
        const formattedMessages2 = formattedMessages.filter(msg => msg.talkid === profile.id);
        if (formattedMessages2 !== messages) {
          setMessages(formattedMessages2);
          const latestMsgs = formattedMessages.reduce((acc, msg) => { if (!acc[msg.talkid] || new Date(msg.date + ' ' + msg.time) > new Date(acc[msg.talkid].date + ' ' + acc[msg.talkid].time)) acc[msg.talkid] = msg; return acc; }, {});
          setLatestMessages(latestMsgs);
        }
      }
    } catch (err) { console.log(err); }
    console.log(messages);
  }
  const clickTalkTitle = () => {
    if (page == 'talk') window.location.replace(`/team`);
    if (page == 'team') window.location.replace(`/talk`);
  }

  // 출력

  const getTitle = () => {
    try {
      if (page == 'team') {
        setText(<h4 className='talk_title'>
          <span className="selected"><Link to={`/team`}>TEAM</Link></span>
          <span><Link to={`/talk`} onClick={clickTalkTitle}>TALK</Link></span>
        </h4>);
        setAdd(<h4 className='add' onClick={goAdd}> </h4>);
      }
      else if (page == 'talk') {
        setText(<h4 className='talk_title'>
          <span><Link to={`/team`} onClick={clickTalkTitle}>TEAM</Link></span>
          <span className="selected"><Link to={`/talk`}>TALK</Link></span>
        </h4>);
        setAdd(<></>);
      }
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

  const getLogin = () => { window.location.replace('/account/login'); }

  const goAdd = () => { navigate('/team/add'); }



  return (
    <>
      <div className="talk_cont">
        <div className="talk_list" >
          {text}
          {(loginUser === '')
            ? <></>
            : <>{add}</>}
          <div className="talk_list_inner">
            {(loginUser === '')
              ? <div className="login">
                <p><em>1초만에</em> 로그인하고 <strong>빈즈톡</strong> 해보세요!</p>
                <button type="button" onClick={getLogin}><span>빈즈하러가기</span></button></div>
              : <>{talkTag()}</>
            }
          </div>
          <div className="detail_contents detail_black_cont"></div>
        </div>
      </div>
    </>
  );
};

export default Talk;