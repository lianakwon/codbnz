import React from 'react'
import Mate from './Mate'
import Today from './Today'
import { Link } from 'react-router-dom'

const Board = () => {
  return (
    <>
      <div className='title_area'>
        <h2>작성글 | 작성댓글</h2>
        <Link to='/my/board' className='more'>더보기</Link>
      </div>
      <Mate />
      <Today />
    </>
  )
}

export default Board