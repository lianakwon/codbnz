import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import edit from '../../../assets/icons/edit_l.png'

const Profile = () => {

  const navigate = useNavigate();

  const clickTitle = () => { navigate('/'); }

  const profile = [
    { title: "아이디", cont: "liana" },
    { title: "비밀번호", cont: "liana199619" },
    { title: "이메일", cont: "kmr199619@gmail.com" },
    { title: "닉네임", cont: "소세지" },
    { title: "상태메세지", cont: "소세지조아" }
  ]



  return (
    <>
      <h2 onClick={clickTitle} className='link_back'>마이페이지</h2>
      <div className='profile_thum'>
        <div className='thum'></div>
        <Link to={`/my/profile_img`}><img src={edit} alt='edit' width={14} /></Link>
      </div>
      <div className='profile_cont'>
        <table>
          <colgroup>
            <col style={{ 'width': '12%' }}></col>
            <col style={{ 'width': '80%' }}></col>
          </colgroup>
          {profile.map((item) => {
            return (
              <tr>
                <th className='title'>{item.title}</th>
                <td>{item.cont}</td>
              </tr>
            )
          })}
          <Link to={`/my/profile`}><img src={edit} alt='edit' width={14} /></Link>
        </table>
      </div>
    </>
  )
}

export default Profile