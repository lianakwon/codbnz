import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';
import edit from '../assets/icons/edit_l.png'
import mypage from '../assets/icons/mypage.png'
import bnzMate from '../assets/icons/bnzMate.png'
import bnzPlan from '../assets/icons/bnzPlan.png'
import bnzTalk from '../assets/icons/bnzTalk.png'
import bnzToday from '../assets/icons/bnzToday.png'
import { componentDidMount } from '../Axios';
import authAxios from '../interceptors';



const Header = () => {

  const navigate = useNavigate();

  const [loginUser, setLoginUser] = useState(null);

  useEffect(() => { componentDidMount().then(res => { setLoginUser(res); }); });

  const logout =async (e) => {
    e.preventDefault();
    const response = await authAxios.post("/account/logout");
    // Handle successful logout response
    console.log('Logout successful:', response.data);
    localStorage.clear();
    setLoginUser('');
    alert("logout");
    window.location.replace('/');
  }

  const myMenuOnclick = (e) => {
    e.preventDefault();
    const myMenu = document.querySelector('.my_menu');
    if (myMenu == null) { } else {
      if (!myMenu.classList.contains('onclick') || myMenu.classList === null) myMenu.classList.add('onclick')
      else myMenu.classList.remove('onclick');
    }
  };



  return (
    <div id="header">
      <div className="inner_l">
        <div className='header_left'>
          <Link to='/'>codbnz</Link>
        </div>
        <div className='header_center'>
          <ul>
            <li><Link to='/mate'><img src={bnzMate} alt='bnzMate' /></Link></li>
            <li><Link to='/today'><img src={bnzToday} alt='bnzToday' /></Link></li>
            <li><Link to='/team'><img src={bnzTalk} alt='bnzTalk' /></Link></li>
            <li><Link to='/plan'><img src={bnzPlan} alt='bnzPlan' /></Link></li>
          </ul>
        </div>
        <div className='header_right'>

          {(loginUser === null || loginUser === undefined || loginUser === '')
            ? (<ul><li></li><li><Link to='/account/login'><img src={edit} alt='login' /></Link></li></ul>)
            : (<ul>
              <li><Link to='/' onClick={logout}><img src={edit} alt='logout' /></Link></li>
              <li><Link to='/my' onClick={myMenuOnclick}><img src={mypage} alt='mypage' /></Link>
                <ul className='my_menu'>
                  <li><Link to={'/my/profile'}>프로필 관리</Link></li>
                  <li><Link to={'/my/team'}>팀 관리</Link></li>
                  <li><Link to={'/my/board'}>작성글 관리</Link></li>
                  <li><Link to={'/my/bookmark'}>북마크 관리</Link></li>
                </ul>
              </li>
            </ul>)}
        </div>
      </div>
    </div>
  );
}

export default Header;