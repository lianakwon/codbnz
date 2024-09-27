import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import Header from '../../layouts/Header';

const Join_2 = () => {

  const navigate = useNavigate();

  const clickTitle = () => { navigate(-1); }

  const [joinDTO, setJoinDTO] = useState({ username: "", password1: "", password2: "", nickname: "", email: "", phone1: "010", phone2: "", phone3: "" });
  const { username, password1, password2, nickname, email, phone1, phone2, phone3 } = joinDTO;

  const onChangeJoin = (e) => { setJoinDTO({ ...joinDTO, [e.target.name]: e.target.value }); };

  const [errorUN, setErrorUN] = useState(" ");
  const [errorPW1, setErrorPW1] = useState(" ");
  const [errorPW2, setErrorPW2] = useState(" ");
  const [errorNN, setErrorNN] = useState(" ");
  const [errorEM, setErrorEM] = useState(" ");
  const [errorPH, setErrorPH] = useState(" ");

  useEffect((e) => { isValid(e); }, [joinDTO])

  function isValid() {
    const isValidPW = (pw) => { const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/; return regex.test(pw); };
    const isValidEM = (em) => { const regex = /^[a-zA-Z0-9+-.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/g; return regex.test(em); };
    const isValidPH = (ph) => { const regex = /^([0-9]{4,4})$/; return regex.test(ph); };

    if (password1 === "" || isValidPW(password1)) { setErrorPW1(" "); }
    else { setErrorPW1("대/소문자, 숫자 포함 8-20자"); }

    if (password1 === password2) { setErrorPW2(" "); }
    else { setErrorPW2("패스워드를 확인해주세요."); }

    if (email === "" || isValidEM(email)) { setErrorEM(" "); }
    else { setErrorEM("올바른 이메일 형식으로 입력해주세요"); }

    if (phone2 === "" && phone3 === "") { setErrorPH(" "); }
    if (isValidPH(phone2) && isValidPH(phone3)) { setErrorPH(" "); }
    else { setErrorPH("전화번호는 숫자 3-4자리로 입력해주세요."); };
  }

  function isValidEvent(e) {
    if (e.target.name === "password1" || e.target.name === "password2") { const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/; return regex.test(e.target.value); };
    if (e.target.name === "email") { const regex = /^[a-zA-Z0-9+-.]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/g; return regex.test(e.target.value); };
    if (e.target.name === "phone1" || e.target.name === "phone2" || e.target.name === "phone3") { const regex = /^([0-9]{3,4})$/; return regex.test(e.target.value); };
  }

  function activeEnter(e) {
    if (e.key === "Enter") {
      e.preventDefault();
      console.log(joinDTO);
      if (e.target.name === "username") { checkUsername(e); }
      if (e.target.name === "password1" && isValidEvent(e)) { document.querySelector("input[id='password2']").focus(); }
      if (e.target.name === "password2" && isValidEvent(e)) { document.querySelector("input[id='nickname']").focus(); }
      if (e.target.name === "nickname") { checkNickname(e); }
      if (e.target.name === "email" && isValidEvent(e)) { document.querySelector("input[id='phone1']").focus(); }
      if (e.target.name === "phone1" && isValidEvent(e)) { document.querySelector("input[id='phone2']").focus(); }
      if (e.target.name === "phone2" && isValidEvent(e)) { document.querySelector("input[id='phone3']").focus(); }
      if (e.target.name === "phone3" && isValidEvent(e)) { submit(e); }
    }
  }

  const checkUsername = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/account/check_username", { name: username })
      .then(response => {
        console.log(response.data);
        if (response.data === 'success') {
          setErrorUN("사용 가능한 아이디입니다.");
          document.querySelector('.checkUsername > .error-message').classList.add('success');
          document.querySelector('input[name="password1"]').focus();
          return true;
        } else {
          setErrorUN("중복되는 아이디입니다.");
          document.querySelector('.checkUsername > .error-message').classList.remove('success');
          document.querySelector('input[name="username"]').focus();
          return false;
        }
      })
      .catch(errors => {
        console.log(errors);
        setErrorUN("중복되는 아이디입니다.");
        document.querySelector('.checkUsername > .error-message').classList.remove('success');
        document.querySelector('input[name="username"]').focus();
        return false;
      });

  }

  const checkNickname = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/account/check_nickname", { name: nickname })
      .then(response => {
        console.log(response.data);
        if (response.data === 'success') {
          setErrorNN("사용 가능한 닉네임입니다.");
          document.querySelector('.checkNickname > .error-message').classList.add('success');
          document.querySelector('input[name="email"]').focus();
          return true;
        } else {
          setErrorNN("중복되는 닉네임입니다.");
          document.querySelector('.checkNickname > .error-message').classList.remove('success');
          document.querySelector('input[name="nickname"]').focus();
          return false;
        }
      })
      .catch(errors => {
        console.log(errors);
        setErrorNN("중복되는 닉네임입니다.");
        document.querySelector('.checkNickname > .error-message').classList.remove('success');
        document.querySelector('input[name="nickname"]').focus();
        return false;
      });

  }

  const submit = async (e) => {
    e.preventDefault();
    if (errorPW1 !== ' ') { document.querySelector("input[id='password1']").focus(); }
    else if (errorPW2 !== ' ') { document.querySelector("input[id='password2']").focus(); }
    else if (errorEM !== ' ') { document.querySelector("input[id='email']").focus(); }
    else if (errorPH !== ' ') { document.querySelector("input[id='phone2']").focus(); }
    else await axios.post("http://localhost:8080/account/join", joinDTO)
      .then(res => { alert("회원가입 완료! 로그인 화면으로 이동합니다."); navigate(`/account/login`); })
      .catch(err => { navigate(`/account/join/2`); document.querySelector('input[name="username"]').focus(); });

  }



  return (
    <>
      <div className='inner_login inner_join'>
        <h1 onClick={clickTitle}>회원가입</h1>
        <form onSubmit={submit}>

          <div className='checkUsername'>
            <input type='text' id='username' name='username' value={username} placeholder='아이디' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <button type="submit" className='check' onClick={checkUsername}>아이디 중복 확인</button>
            <p className="error-message success">{errorUN}</p></div>

          <div>
            <input type='password' id='password1' name='password1' value={password1} placeholder='비밀번호' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <p className="error-message">{errorPW1}</p></div>

          <div>
            <input type='password' id='password2' name='password2' value={password2} placeholder='비밀번호 확인' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <p className="error-message">{errorPW2}</p></div>

          <div className='checkNickname'>
            <input type='text' id='nickname' name='nickname' value={nickname} placeholder='닉네임' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <button type="submit" className='check' onClick={checkNickname}>닉네임 중복 확인</button>
            <p className="error-message success">{errorNN}</p></div>

          <div>
            <input type='text' id='email' name='email' value={email} placeholder='이메일' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <p className="error-message">{errorEM}</p></div>

          <div className="phone">
            <input type='text' id='phone1' name='phone1' value={phone1} placeholder='010' onChange={onChangeJoin} onKeyDown={activeEnter} />-
            <input type='text' id='phone2' name='phone2' value={phone2} placeholder='0000' onChange={onChangeJoin} onKeyDown={activeEnter} />-
            <input type='text' id='phone3' name='phone3' value={phone3} placeholder='0000' onChange={onChangeJoin} onKeyDown={activeEnter} />
            <p className="error-message">{errorPH}</p></div>

          <div>
            <button type="submit">회원가입</button></div>

          <div>
            <p> </p>
            <p> </p>
          </div>
        </form>
      </div></>
  )
}

export default Join_2