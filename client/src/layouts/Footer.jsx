import React from 'react'

const Footer = () => {

  function myOnclick() {
    const myMenu = document.querySelector('.my_menu');
    if (myMenu.classList.contains('onclick')) myMenu.classList.remove('onclick');
  }



  return (
    <div id="footer" onClick={myOnclick}>
      <div className="inner_l">
        <p>@codbnz</p>
      </div>
    </div>
  )
}

export default Footer