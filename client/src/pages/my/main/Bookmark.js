import React from 'react'
import Mate from './Mate'
import Today from './Today'
import { Link } from 'react-router-dom'

const Bookmark = () => {
  return (
    <>
      <div className='title_area'>
        <h2>좋아요 | 궁금해요</h2>
        <Link to='/my/bookmark' className='more'>더보기</Link>
      </div>
      <Mate />
      <Today />
    </>
  )
}

export default Bookmark