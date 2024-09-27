import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import bubbles from "../../../assets/images/bubbles.png";
import axios from 'axios';
import { componentDidMount } from '../../../Axios';



const Update = () => {

  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => { navigate(-1); }

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState("");

  // ★ 공통사항 ★ 페이지 로딩 완료 시점 : 유저 정보 조회 → setLoginUser() → get() 실행
  // ★ get(res) ★ 경우에 따라 실행시킬 함수의 이름을 넣어주세용. 페이지 로딩과 동시에 실행될 함수 삽입, 없으면 삭제
  useEffect(() => { componentDidMount().then(res => { setLoginUser(res); get(res); }).catch(setLoginUser("")); }, []);

  // profileTXT(내정보), profileIMG(내프사) : 기본세팅 + 구조분해할당
  const [profileTXT, setProfileTXT] = useState({ username: "", email: "", nickname: "", profileMSG: "" });
  const [profileSRC, setProfileSRC] = useState({ shape: "", color: "", eye: "", face: "" });
  const [profileIMG, setProfileIMG] = useState("");
  const { username, email, nickname, profileMSG } = profileTXT || {};
  const { shape, color, eye, face } = profileSRC || {};

  // profileIMG : shapes, colors, eyes, faces : 기본세팅
  const shapes = ['Round', 'Dump', 'Long', 'Big'];
  const colors = ['Red', 'Yel', 'Grn', 'Blu'];
  const eyes = ['Small', 'Shine', 'Long', 'Big'];
  const faces = ['Soso', 'Smile', 'Ah', 'Oh'];

  // profileTXT(내정보), onChangeSRC(내프사) : input onChange => setProfile
  const onChangeTXT = (e) => { setProfileTXT({ ...profileTXT, [e.target.name]: e.target.value }); };
  const onChangeSRC = (e) => { setProfileSRC({ ...profileSRC, [e.target.className]: e.target.alt }); };
  useEffect(() => { setProfileIMG(shape + color + eye + face + ".png"); }, [shape, color, eye, face]);



  async function get(loginUser) {
    console.log(loginUser);
    if (loginUser === undefined) { alert('로그인 해주세요.'); navigate('/account/login'); }
    const res = (await axios.get(`http://localhost:8080/my/get_account/${loginUser}`)).data;
    console.log(res);
    setProfileTXT({ username: res.username, email: res.email, nickname: res.nickname, profileMSG: res.profileMSG });
    setProfileSRC({ shape: res.shape, color: res.color, eye: res.eye, face: res.face });
  };

  const submitTXT = async (e) => {
    e.preventDefault();
    await axios.post(`http://localhost:8080/my/change_txt/${loginUser}`
      , { username: username, email: email, nickname: nickname, profileMSG: profileMSG })
      .then(() => { alert('프로필 변경 성공!'); localStorage.setItem("loginNickname", nickname); })
      .catch(err => { console.log(err); });
  };

  const submitIMG = async (e) => {
    e.preventDefault();
    await axios.post(`http://localhost:8080/my/change_img/${loginUser}`
      , { shape: shape, color: color, eye: eye, face: face })
      .then(() => { alert('프로필 이미지 변경 성공!'); })
      .catch(err => { console.log(err); });
  };




  return (
    <><div id='my' className="inner_m"><div id='profile'><h2 onClick={clickTitle} className='link_back'>프로필 편집</h2>

      <div className='profile_thum'><div className='thum'><h3><img src={`http://localhost:3000/profile/${profileIMG}`} /></h3></div></div>

      <form onSubmit={submitTXT} className='profile_cont update'>
        {/* 아이디 */}
        <div><label htmlFor={`username`} className='title'>아이디</label>
          <input type='text' value={username || ""} name='username' id='username' onChange={onChangeTXT} disabled /></div>
        {/* 비밀번호 */}
        <div><label className='title'>비밀번호</label><input type='text' value="********" disabled /></div>
        {/* 이메일 */}
        <div><label htmlFor={`email`} className='title'>이메일</label>
          <input type='text' value={email || ""} name='email' id='email' onChange={onChangeTXT} disabled /></div>
        {/* 닉네임 */}
        <div><label htmlFor={`nickname`} className='title'>닉네임</label>
          <input type='text' value={nickname || ""} name='nickname' id='nickname' onChange={onChangeTXT} /></div>
        {/* 상태메세지 */}
        <div><label htmlFor={`profileMSG`} className='title'>상태메세지</label>
          <input type='text' value={profileMSG || ""} name='profileMSG' id='profileMSG' onChange={onChangeTXT} /></div>
        <div className='buttons'>
          <Link to="/">취소</Link><button type='submit'>수정</button></div></form></div>

      <form onSubmit={submitIMG}>
        {/* shape */}
        <div className='select shape'><ul><p className='title'>모양</p>
          {shapes.map((item, key) => { return <li key={key}><Link><img src={bubbles} className='shape' onClick={onChangeSRC} alt={item} /></Link></li> })}</ul></div>
        {/* color */}
        <div className='select color'><ul><p className='title'>색</p>
          {colors.map((item, key) => { return <li key={key}><Link><img src={bubbles} className='color' onClick={onChangeSRC} alt={item} /></Link></li> })}</ul></div>
        {/* eye */}
        <div className='select eyes'><ul><p className='title'>눈</p>
          {eyes.map((item, key) => { return <li key={key}><Link><img src={bubbles} className='eye' onClick={onChangeSRC} alt={item} /></Link></li> })}</ul></div>
        {/* face */}
        <div className='select face'><ul><p className='title'>표정</p>
          {faces.map((item, key) => { return <li key={key}><Link><img src={bubbles} className='face' onClick={onChangeSRC} alt={item} /></Link></li> })}</ul></div>
        <div className='buttons'>
          <Link to="/" style={{ textAlign: 'center' }}>취소</Link><button type='submit'>수정</button></div></form></div></>
  )
}

export default Update