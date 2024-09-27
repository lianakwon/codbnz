import React, { useEffect, useState } from 'react'
import Profile from './Profile'
import Team from './Team'
import Bookmark from './Bookmark'
import Board from './Board'
import { useNavigate } from 'react-router-dom/dist'
import { componentDidMount } from '../../../Axios'
import axios from 'axios'

const My = () => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { navigate(-1); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState("");

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => { componentDidMount().then(res => { if (res === undefined) { navigate('/account/login'); }; }).catch(setLoginUser("")); }, []);

  return (
    <>
      <div id='my' className="inner_m">
        <div id='profile'><Profile /></div>
        <div id='team'><Team /></div>
        <div id='bookmark'><Bookmark /></div>
        <div id='board'><Board /></div>
      </div>
    </>
  )
}

export default My