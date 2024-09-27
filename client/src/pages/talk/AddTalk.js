import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import { componentDidMount } from '../../Axios';
import axios from 'axios';
import authAxios from '../../interceptors';

const AddTalk = () => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { window.location.replace('/team'); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState('');
  const [profile, setProfile] = useState({ id: 0, nickname: '' });

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => {
    componentDidMount().then(res => {
      if (res === undefined || res === null) { } else {
        setLoginUser(res); getLoginUser(res);
      }
    }).catch(setLoginUser(''));
  }, []);

  const getLoginUser = async (loginUser) => {
    const res = (await authAxios.get(`http://localhost:8080/my/get_account/${loginUser}`)).data;
    setProfile({ id: res.id, nickname: res.nickname });
    setManager(res.nickname);
  }

  const [manager, setManager] = useState('');
  const [title, setTitle] = useState('');
  const [intro, setIntro] = useState('');

  const inputOnChange = (e) => {
    if (e.target.name == 'title') setTitle(e.target.value);
    if (e.target.name == 'intro') setIntro(e.target.value);
    console.log("title : " + title + ", intro : " + intro)
  }

  const submitForm = async (e) => {
    e.preventDefault();
    const res = (await authAxios.post(`http://localhost:8080/team/create_team/${loginUser}`,
      { id: null, team_title: title, team_intro: intro }));
    alert("팀 생성 성공! 팀명 : " + title);
    navigate('/team');
  }





  return (
    <>
      <div className="talk_cont">
        <div className="talk_list" >
          <div className="talk_list_inner">
            <h4 className='talk_title link_back' onClick={clickTitle}>빈즈팀 만들기</h4>
            <form className='add_team' onSubmit={submitForm}>
              <table>
                <tbody>
                  <tr>
                    <td><label htmlFor='manager'>매니저</label></td>
                    <td><input type='text' name='manager' value={manager} className='read_only' readOnly onChange={inputOnChange} /></td></tr>
                  <tr>
                    <td><label htmlFor='title'>팀명</label></td>
                    <td><input type='text' name='title' value={title} placeholder='팀 이름을 입력하세요' onChange={inputOnChange} /></td></tr>
                  <tr>
                    <td><label htmlFor='intro'>팀 소개</label></td>
                    <td><textarea type='text' name='intro' value={intro} placeholder='팀 소개를 입력하세요' rows={'5'} onChange={inputOnChange} /></td></tr>
                  <tr>
                    <td><button type='submit'>등록</button><Link to={`/team`}>취소</Link></td>
                    <td></td></tr>
                </tbody>
              </table>






            </form>
          </div>
          <div className="detail_contents detail_black_cont"></div>
        </div>
      </div>
    </>
  )
}

export default AddTalk