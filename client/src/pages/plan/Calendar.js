import React, { useState, useEffect, useContext } from 'react';
import { Link } from 'react-router-dom';
import PopupEvent from './PopupEvent';
import { AuthContext } from '../../AuthProvider';
import {HttpHeadersContext} from '../../HttpHeadersProvider';
import { componentDidMount } from '../../Axios';
import mypage from '../../assets/icons/mypage.png';
import back from '../../assets/icons/back.png';
import authAxios from '../../interceptors';


const Calendar = () => {

  const { auth, setAuth } = useContext(AuthContext);
  const { headers, setHeaders} = useContext(HttpHeadersContext);

  // ★ 공통사항 ★ loginUser(로그인한 사용자) : 기본세팅
  const [loginUser, setLoginUser] = useState('');
  // profileTXT(내정보), profileIMG(내프사) : 기본세팅 + 구조분해할당
  const [profileTXT, setProfileTXT] = useState({ username: "", email: "", nickname: "", profileMSG: "" });
  
  // 현재 날짜 상태 및 초기 달력 상태
  const [currentDate, setCurrentDate] = useState(new Date()); // 현재 날짜를 상태로 관리합니다.
  const [headerMonth, setHeaderMonth] = useState('');
  const [headerYear, setHeaderYear] = useState('');
  const [calendarBody, setCalendarBody] = useState([]);
  const [showPopup, setShowPopup] = useState(false);
  const [clickedDate, setClickedDate] = useState([]);
  const [userSelectedColor, setUserSelectedColor] = useState([]); // 사용자가 선택한 색상값 상태 (해당 저장한 일정에 색상 보여주기)
  const [topPlanIdDesc, setTopPlanIdDesc] = useState([]); // 브라우저에 최신 일정 5개까지 보여주기
  const [eventDataFetched, setEventDataFetched] = useState(false);

  const [title, setTitle] = useState('');
  const [color, setColor] = useState('');
  const [summary, setSummary] = useState('');
  const [startingHour, setStartingHour] = useState('');
  const [endingHour, setEndingHour] = useState('');
  const [planId, setPlanId] = useState(null);

  const [isYearSelectionMode, setIsYearSelectionMode] = useState(false);
  const [showLatestEvents, setShowLatestEvents] = useState(true); // 최신 일정 또는 전체 일정 표시 여부 상태

  // 컴포넌트가 마운트될 때 초기화 함수 실행
  useEffect(() => {
    componentDidMount().then(res => {
      setLoginUser(res);
      get(res);
    }).catch(() => setLoginUser(""));

    updateCalendar(); // useEffect 훅을 사용하여 컴포넌트가 마운트될 때 초기화 함수를 실행합니다.
      if (!eventDataFetched) {
        findAllEvent();
        findByPlanIdTopDesc();
        setEventDataFetched(true);
    }

    // esc를 눌렀을 경우 클릭이벤트 종료
    const handleEscape = (event) => {
      if (event.keyCode === 27) {
        setIsYearSelectionMode(false);
        setShowPopup(false);
      }
    };
    window.addEventListener('keydown', handleEscape);

    setHeaders({
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    });

    return () => {
      window.removeEventListener('keydown', handleEscape);
    };
    
  }, [userSelectedColor, eventDataFetched, setHeaders]);


  // 현재 날짜와 보기에 기반하여 달력을 업데이트하는 함수
  const updateCalendar = () => {
    const month = currentDate.getMonth(); // 현재 월을 가져옵니다.
    const year = currentDate.getFullYear(); // 현재 연도를 가져옵니다.

    // 월과 연도 표시 업데이트
    setHeaderMonth(month + 1); // 월을 표시하는 상태를 업데이트합니다.
    setHeaderYear(year); // 연도를 표시하는 상태를 업데이트합니다.

    // 달력 그리드 초기화 및 업데이트
    const rows = generateMonthlyCalendar(month); // 해당 월에 대한 달력 그리드를 생성합니다.
    setCalendarBody(rows); // 생성된 달력 그리드를 상태로 설정합니다.
  };

  // // 월 이름 가져오는 함수
  // const getMonthName = (monthIndex) => {
  //   const months = [
  //     '01', '02', '03', '04', '05', '06',
  //     '07', '08', '09', '10', '11', '12'
  //   ];
  //   return months[monthIndex]; // 월 이름을 반환합니다.
  // };

  // 특정 월에 대한 달력 그리드를 생성하는 함수
  const generateMonthlyCalendar = (monthIndex) => {
    const month = monthIndex; // 선택된 월을 가져옵니다.
    const year = currentDate.getFullYear(); // 현재 연도를 가져옵니다.

    // 해당 월의 시작 요일과 총 일수를 가져옵니다.
    const startingDay = new Date(year, month, 1).getDay(); // 해당 월의 첫째 날의 요일을 가져옵니다.
    const totalDays = new Date(year, month + 1, 0).getDate(); // 해당 월의 총 일수를 가져옵니다.

    let date = 1; // 달력에 표시될 날짜를 초기화합니다.
    const calendarRows = []; // 달력 행을 담을 배열을 선언합니다.

    for (let row = 0; row < 6; row++) { // 달력은 최대 6주까지 표시됩니다.
      const calendarCells = []; // 각 달력 셀을 담을 배열을 선언합니다.

      for (let col = 0; col < 7; col++) { // 요일은 7일입니다.
        if (row === 0 && col < startingDay) {
          // 월의 첫째 날 이전의 빈 셀
          calendarCells.push(<td key={`${row}-${col}`} className="empty"></td>); // 빈 셀을 추가합니다.
        } else if (date > totalDays) {
          // 월의 마지막 날 이후의 빈 셀
          calendarCells.push(<td key={`${row}-${col}`} className="empty"></td>); // 빈 셀을 추가합니다.
        } else {
          // 해당 월의 각 날짜에 대한 셀
          const isToday = date === new Date().getDate() && month === new Date().getMonth() && year === new Date().getFullYear();
          let clickYear = year;
          let clickMonth = month + 1;
          let clickDate = date;
          let checkDate = `${clickYear}-${clickMonth > 9 ? clickMonth : "0" + clickMonth}-${clickDate > 9 ? clickDate : "0" + clickDate}`;
          calendarCells.push(
            <td key={`${row}-${col}`} onClick={() => handleClick(clickYear, clickMonth, clickDate)} className={isToday ? "calendarToday" : ""}>
              {date}
              {userSelectedColor.map(item => {
                if (item.date === checkDate) {
                  return (
                    <span key={item.date} className='circleColor' style={{ backgroundColor: item.color }}></span>
                  );
                }
                return null; // 조건이 맞지 않는 경우에는 null을 반환하여 렌더링하지 않음
              })}
              
            </td>
          );
          date++; // 다음 날짜로 이동합니다.
        }
      }

      calendarRows.push(<tr key={row}>{calendarCells}</tr>); // 달력 행을 추가합니다.
    }

    return calendarRows;
  };

  // 클릭 시 이전 달로 이동
  const handlePrevMonthClick = (month, year) => {
    if (updateCalendar()) {
        // 현재 날짜
      const currentDay = currentDate.getDate();
      let daysToSubtract = 7;
      const startingDay = currentDay - 6;
      const realMonthStartingDay = new Date(year, month, 1).getDay(); // 월의 첫째 날
      if (startingDay < realMonthStartingDay) {
        daysToSubtract = realMonthStartingDay - startingDay;
      }
      currentDate.setDate(currentDay - daysToSubtract);
      updateCalendar();
    } else {
      currentDate.setMonth(month - 1);
    }
    updateCalendar();
  };


  // 클릭 시 다음 달로 이동
  const handleNextMonthClick = (month, year) => {
    if(updateCalendar()){
      const currentDay = currentDate.getDate();
      let daysToAdd = 7;
      const endingDate = currentDay + 6;
      const realMonthEndingDay = new Date(year, month + 1, 0).getDate(); // 월의 마지막 날
      if(endingDate > realMonthEndingDay){
        daysToAdd = endingDate - realMonthEndingDay;
      }
      currentDate.setDate(currentDate + daysToAdd);
      updateCalendar();
    }else{
      currentDate.setMonth(month + 1);
    }
    updateCalendar();
  };


  // 사용자가 원하는 날짜를 클릭 시 팝업창이 나오고 해당 날짜를 물고 가게 한다.
  const handleClick = (year, month, date) => {
    // 로그인한 사용자인지 체크
    if (!profileTXT) {
        alert("로그인 한 사용자만 캘린더에 일정을 작성할 수 있습니다 !");
        // Navigate(-1);
    }else{
    console.log("클릭된 날짜 정보:", { year, month, date });
    // 클릭된 날짜 정보를 상태에 설정하고 팝업 창 열기
    setClickedDate({ year, month, date });
    // console.log(userSelectedColor);
    setShowPopup(true);
    }
    
  };

  // 팝업창 닫기
  const handleClosePopup = () => {
    setShowPopup(false);
    setClickedDate(null);
  };

  // 전체 데이터 가져오기
  const findAllEvent = async () => {
    if(get){
    await authAxios.get('/plan', { headers: headers })
    .then(response => {
      // 해당 사용자 전체 데이터 가져오기
      const colors = Array.isArray(response.data) ? response.data : [] ;
      setUserSelectedColor(colors);
      console.log(colors); // 색상값 배열 출력
      findByPlanIdTopDesc();
      console.log("전체 목록");
    })
    }else{
      console.log("캘린더 전체 목록 보여주기 실패");
    }

  }

  const findByPlanIdTopDesc = async () => {
    try {
      const response = await authAxios.get('/plan/planIdTop5Desc', { headers });
      const data = Array.isArray(response.data) ? response.data : [];
      setTopPlanIdDesc(data);
    } catch (error) {
      console.error('Failed to fetch top plan IDs', error);
      setTopPlanIdDesc([]);
    }
  };

  const handlePlanClick = async (planId) => {
    try {
      const response = await authAxios.get(`/plan/${planId}`, { headers: headers });
      const eventData = response.data;

      setClickedDate({
        year: new Date(eventData.date).getFullYear(),
        month: new Date(eventData.date).getMonth() + 1,
        date: new Date(eventData.date).getDate()
      });

      setTitle(eventData.title);
      setColor(eventData.color);
      setSummary(eventData.summary);
      setStartingHour(eventData.startingHour);
      setEndingHour(eventData.endingHour);
      setPlanId(eventData.id);
      setShowPopup(true);
    } catch (error) {
      console.error('일정을 불러오는데 실패했습니다.', error);
    }
  };

  // getAccount : 프로필 정보 조회
  async function get(loginUser) {
    console.log(loginUser);
    const res = (await authAxios.get(`/my/get_account/${loginUser}`)).data;
    console.log(res);
    setProfileTXT({ username: res.username, email: res.email, nickname: res.nickname, profileMSG: res.profileMSG });
  };


  const handleYearSelect = (year) => {
    const newDate = new Date(currentDate.setFullYear(year));
    setCurrentDate(newDate);
    setIsYearSelectionMode(false);
    updateCalendar();
  };

  // YearSelector 컴포넌트: 사용자가 연도를 선택할 수 있는 인터페이스를 제공
  const YearSelector = () => {
    // 현재 연도에서 -34년(1990년)부터 +10년까지 총 12개의 연도를 생성하는 배열 생성
    const years = Array.from({ length: 45 }, (_, i) => headerYear - 34 + i);

    return (
      // 연도 선택을 위한 div 컨테이너
      <ul className="year-selector">
        {years.map((year) => (
          // 각 연도를 나타내는 span 요소 생성
          // 클릭 시 handleYearSelect(year) 함수를 호출하여 해당 연도로 달력을 업데이트
          <li className='cilck_year' key={year} onClick={() => handleYearSelect(year)}>
            {year}년
          </li>
        ))}
      </ul>
    );
  };

  const cilckYearMonth = () => {
    setIsYearSelectionMode(true);
  };

return (
  <>
    <div className="inner_m" id="plan">
      <h1>빈즈플래너</h1>
      <div className="planner_left">
        <div className="calendar">
          <div className="header_calendar" id="header_calendar">
            <span id="headerYearMonth" onClick={cilckYearMonth}>
              {headerYear}년 {headerMonth}월
            </span>
            <div className="MonthBtn">
              <button
                id="prevMonthBtn"
                onClick={() =>
                  handlePrevMonthClick(
                    currentDate.getMonth(),
                    currentDate.getFullYear()
                  )
                }
              >
                <img src={back} alt="back" />
              </button>
              <button
                id="nextMonthBtn"
                onClick={() =>
                  handleNextMonthClick(
                    currentDate.getMonth(),
                    currentDate.getFullYear()
                  )
                }
              >
                <img src={back} alt="back" />
              </button>
            </div>
          </div>

          {isYearSelectionMode ? <YearSelector /> : ""}
          <table id="calendarTable">
            <thead id="calendarDays">
              <tr id="calendarDaysRow">
                <th className="days">일</th>
                <th className="days">월</th>
                <th className="days">화</th>
                <th className="days">수</th>
                <th className="days">목</th>
                <th className="days">금</th>
                <th className="days">토</th>
              </tr>
            </thead>
            <tbody id="calendarBody">{calendarBody}</tbody>
          </table>
        </div>
        {showPopup && (
          <PopupEvent
            clickedDate={clickedDate}
            onClose={handleClosePopup}
            onEventChange={findAllEvent}
          />
        )}
      </div>
      <div className="planner_right">
        <div className="planner_auth">
          <p>{profileTXT.nickname}님</p>
          <span>
            <Link to="/my/profile">
              <img src={mypage} alt="mypage" />
            </Link>
          </span>
          <i>{profileTXT.profileMSG}</i>
        </div>


        <div className="Latest_planId">
        <button className='Latest_togger_btn' onClick={() => setShowLatestEvents(!showLatestEvents)}>
            {showLatestEvents ? "전체 일정 확인하기" : "최신 일정 확인하기"}{" "}
            {/* 수정: 토글 버튼 */}
          </button>
          <div className="Latest_planId_list">
            {showLatestEvents ? ( // 수정: 최신 일정 또는 전체 일정 표시 여부를 조건부 렌더링
              topPlanIdDesc.length === 0 ? (
                <p>등록하신 일정이 없습니다.</p>
              ) : (
                <>
                  {topPlanIdDesc.map((item) => (
                    <div key={item.id} onClick={() => handlePlanClick(item.id)}>
                      <span
                        className="planId_list_color"
                        style={{ backgroundColor: item.color }}
                      ></span>
                      <i className="planId_list_title">{item.title}</i>
                      <span className="planId_list_date">{item.date}</span>
                      <span className="planId_list_startTime">
                        {item.startingHour}
                      </span>
                      <p className="planId_list_summary">{item.summary}</p>
                    </div>
                  ))}
                </>
              )
            ) : (
              <div className="Latest_planId_list">
                {/* 전체 일정 출력 */}
                {userSelectedColor.map((item) => (
                  <div key={item.id} onClick={() => handlePlanClick(item.id)}>
                    <span
                      className="planId_list_color"
                      style={{ backgroundColor: item.color }}
                    ></span>
                    <i className="planId_list_title">{item.title}</i>
                    <span className="planId_list_date">{item.date}</span>
                    <span className="planId_list_startTime">
                      {item.startingHour}
                    </span>
                    <p className="planId_list_summary">{item.summary}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  </>
);
};

export default Calendar;
