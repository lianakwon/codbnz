import React from 'react'
import { Link } from 'react-router-dom';

const Team = () => {
  const teams = [
    { id: 1, title: "team_1", members: ["mem01", "mem02", "mem03", "mem04"] },
    { id: 2, title: "team_2", members: ["mem01", "mem02", "mem03"] },
    { id: 3, title: "team_3", members: ["mem01", "mem02"] },
    { id: 4, title: "team_4", members: ["mem01"] },
    { id: 5, title: "team_5", members: ["mem01", "mem02", "mem03", "mem04"] },
    { id: 6, title: "team_6", members: ["mem01", "mem02", "mem03"] },
    { id: 7, title: "team_7", members: ["mem01", "mem02"] },
    { id: 8, title: "team_8", members: ["mem01"] }
  ];

  function teamTag() {
    return (
      teams.map((team, index) => {
        return (
          <li key={team.id}>
            <Link to={`/team/${team.id}`}>
              <p className='thumb'></p>
              <p className='title'>{team.title}</p>
              <p className='content'>{memberTag(index)}</p>
            </Link></li>)
      })
    )
  }

  function memberTag(index) {
    const team = teams[index];
    return (
      team.members.map((member) => {
        return (
          <p className='member' key={index}>{member}</p>)
      })
    )
  }



  return (
    <>
      <div className='team'>
        <div className='title_area'>
          <h2>스터디 | 프로젝트</h2>
          <Link to='/my/team' className='more'>더보기</Link>
        </div>
        <ul>
          {teamTag()}
        </ul>
      </div >
    </>
  )
}

export default Team