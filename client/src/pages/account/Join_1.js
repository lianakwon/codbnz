import React from 'react'
import { useNavigate } from 'react-router-dom';
import Header from '../../layouts/Header';

const Join_1 = () => {
  const navigate = useNavigate();
  const clickTitle = () => {
    navigate(-1);
  }

  const submit = (e) => {
    e.preventDefault();
    navigate(`/account/join/2`);
  }


  return (
    <>
    <div className='inner_login'>
      <h1 onClick={clickTitle}>약관동의</h1>
      <form action='join1'>
        {/* <div> */}
          <p className='agree'>약관 1</p>
        {/* </div>
        <div> */}
          <p className='agree'>약관 2</p>
        {/* </div>
        <div> */}
          <p className='agree choice'>약관 3</p>
        {/* </div> */}
        <div>
          <button type="submit" onClick={submit}>다음 단계</button>
        </div>
        <div>
          <p> </p>
          <p> </p>
        </div>
      </form>
    </div></>
  )
}

export default Join_1