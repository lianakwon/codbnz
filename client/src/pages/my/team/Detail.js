import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { componentDidMount } from '../../../Axios';

const Detail = () => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { navigate(-1); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState("");

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => {
    componentDidMount().then(res => {
      if (res === undefined) { navigate('/account/login'); }
      else { setLoginUser(res); getTeams(res); getInvites(res); }
    }).catch(setLoginUser(""));
  }, []);

  // getTeams : 팀 정보 조회
  async function getTeams(id) {
    // if (loginUser === undefined) { alert('로그인 해주세요.'); navigate('/account/login'); }
    try {
      const res = (await axios.get(`http://localhost:8080/my/get_teams/${id}`));
      console.log(res);
      const temp = res.data.map(team => ({ id: team.id, title: team.team_title }));
      setTeams(temp)
    } catch (err) { console.log(err); }
    console.log(teams);
  };

  // getTeams : 초대 정보 조회
  async function getInvites(id) {
    console.log(id);
    try {
      const res = (await axios.get(`http://localhost:8080/my/get_invites/${id}`));
      console.log(res);
      const temp = res.data.map(team => ({ id: team.id, title: team.team_title }));
      setInvites(temp);
    } catch (err) { console.log(err); }
    console.log(invites);
  };

  const [teams, setTeams] = useState([]);
  const [invites, setInvites] = useState([]);

  const accept = async (e) => {
    e.preventDefault();
    try {
      const res = (await axios.put(`http://localhost:8080/member/get_in_team/${loginUser}`, { target_id: loginUser, team_id: e.target.id }));
      console.log(res);
      alert('팀 초대를 수락했습니다.');
      window.location.replace("/my/team/")
    } catch (err) { console.log(err); }
  };

  const reject = async (e) => {
    e.preventDefault();
    try {
      const res = (await axios.put(`http://localhost:8080/member/get_in_not_team/${loginUser}`, { target_id: loginUser, team_id: e.target.id }));
      console.log(res);
      alert('팀 초대를 거절했습니다.');
      window.location.replace("/my/team/")
    } catch (err) { console.log(err); }
  };



  return (
    <>
      <div id='my' className="inner_m detail">
        <div id='team'>
          {(teams.length < 1)
            ? (
              <div className='team'>
                <h2 className='link_back' onClick={clickTitle}>스터디/프로젝트</h2>
                <p className='empty'>소속된 스터디/프로젝트가 없습니다</p>
              </div>
            ) : (
              <div className='team'>
                <h2 onClick={clickTitle} className='link_back'>스터디 | 프로젝트</h2>
                <ul>
                  {teams.map((team, key) => {
                    return (<li key={key}>
                      <Link to={`/team/${team.id}`}>
                        <p className='thumb'></p>
                        <p className='title'>{team.title}</p>
                        <p className='more'>팀 채팅 보기</p>
                      </Link></li>)
                  })}
                </ul>
              </div>
            )}

          {(invites.length < 1)
            ? (
              <div className='team'>
                <h2>초대 받은 스터디/프로젝트</h2>
                <p className='empty'>초대받은 스터디/프로젝트가 없습니다</p>
              </div>
            ) : (
              <div className='team'>
                <h2>초대 받은 팀 목록</h2>
                <ul>
                  {invites.map((invite, key) => {
                    return (<li key={key}>
                      <p className='thumb'></p>
                      <p className='title'>{invite.title}</p>
                      <button type='button' onClick={accept} id={invite.id} className='accept'>수락</button>
                      <button type='button' onClick={reject} id={invite.id} className='reject'>거절</button>
                    </li>)
                  })}
                </ul>
              </div>
            )}

        </div>
      </div>
    </>
  )
}

export default Detail