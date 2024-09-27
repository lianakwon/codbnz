import axios from 'axios'
import { useContext, useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

//== header, auth ==//
import { AuthContext } from '../../AuthProvider';
import { HttpHeadersContext } from '../../HttpHeadersProvider';



const Login = () => {

  const { auth, setAuth } = useContext(AuthContext);
  const { headers, setHeaders } = useContext(HttpHeadersContext);

  const navigate = useNavigate();

  const clickTitle = () => { navigate(-1); }

  const [username, setusername] = useState("");
  const [password, setpassword] = useState("");
  const [errorUN, setErrorUN] = useState("");
  const [errorPW, setErrorPW] = useState("");

  useEffect(() => { document.querySelector("input[name='username']").focus(); }, []);

  const onChangeUsername = (e) => { setusername(e.target.value); };
  const onChangePassword = (e) => { setpassword(e.target.value); };

  useEffect(() => { isValid(); }, [username, password]);

  function isValid() {
    const isValidUN = (un) => { const regex = /^[a-z]+[a-z0-9]{5,11}$/g; return regex.test(un); };
    const isValidPW = (pw) => { const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/; return regex.test(pw); };
    if (username === "" || isValidUN(username)) { setErrorUN(""); } else { setErrorUN("영소문자, 숫자 포함 6-13자"); }
    if (password === "" || isValidPW(password)) { setErrorPW(""); } else { setErrorPW("대/소문자, 숫자 포함 8-20자"); }
  };

  function activeEnter(e) {
    if (e.key === "Enter") {
      e.preventDefault();
      if (e.target.name === "username") { document.querySelector('input[name="password"]').focus(); }
      if (e.target.name === "password") { postLogin(e); }
    }
  }

  const postLogin = async (e) => {
    e.preventDefault();
    console.log("post : " + username + " + " + password);
    await axios.post("http://localhost:8080/account/login", { username: username, password: password })
      .then(res => {
        console.log(res.data);
        alert(`${res.data.nickname}님 반갑습니다!`);
        localStorage.setItem("accessToken", res.data.accessToken);
        localStorage.setItem("loginNickname", res.data.nickname);
        localStorage.setItem("id", res.data.username);
        setAuth(res.data.username); // 사용자 인증 정보(아이디 저장)
        setHeaders({ "Authorization": `Bearer ${res.data.accessToken}` }); // 헤더 Authorization 필드 저장
        window.location.replace(`/`);
      }).catch(err => {
        console.log(err);
        navigate(`/account/login`);
        alert(`아이디/패스워드를 확인해주세요.`);
        document.querySelector('input[name="username"]').focus();
      })
  };

  const oauth = async (e) => {
    e.preventDefault();
    await axios.post(`http://localhost:8080/oauth/${e.target.className}`)
      .then(res => { console.log(res); alert(`로그인 완료! : ${e.target.className}`); navigate(`/`); })
      .catch(err => { console.log(err); alert(`로그인 완료! : ${e.target.className}`); })
  }

// oauth
  const oauthGoogleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google"
  }

  const oauthNaverLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/naver"
  }

  const oauthKakaoLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/kakao"
  }



  return (
    <>
      <div className='inner_login'>
        <h1 onClick={clickTitle}>로그인</h1>
        <form onSubmit={postLogin}>
          <div>
            <input type='text' id='username' name='username' placeholder='아이디' value={username} onChange={onChangeUsername} onKeyDown={activeEnter} />
            {errorUN ? (<p className="error-message id">{errorUN}</p>) : (<p className="error-message id"> </p>)}
          </div>
          <div>
            <input type='password' id='password' name='password' placeholder='비밀번호' value={password} onChange={onChangePassword} onKeyDown={activeEnter} />
            {errorPW ? (<p className="error-message pw">{errorPW}</p>) : (<p className="error-message pw"> </p>)}
          </div>
          <div>
            <button type="submit">로그인</button>
          </div>
        </form>
      </div>

      <div className='inner_login bottom'>
        <div className='left'>
          <Link to='/account/join/1'>회원가입</Link>
        </div>
        <div className='right'>
          <a onClick={oauthKakaoLogin} className='kakao'>KAKAO</a>
          <a onClick={oauthGoogleLogin} className='google'>Google</a>
          <a onClick={oauthNaverLogin} className='naver'>NAVER</a>
        </div>
      </div>
    </>
  )
}

export default Login