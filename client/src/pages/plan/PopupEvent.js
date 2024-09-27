import React, { useState, useEffect, useContext } from 'react';
import axios from 'axios';
import {HttpHeadersContext} from '../../HttpHeadersProvider';
import popupClose from '../../assets/icons/close.png';
import authAxios from '../../interceptors';


const PopupEvent = ({ clickedDate, onClose, onEventChange }) => {

  const { headers, setHeaders} = useContext(HttpHeadersContext);

  const colors = ['#FF8888', '#FFAA80', '#FFD173', '#B0D977', '#66CCAA','#79CAF2', '#61AAF2', '#9997FF', '#dddddd', '#333333'];
  const [title, setTitle] = useState('');
  const [color, setColor] = useState('');
  // const [date, setDate] = useState('');
  const [summary, setSummary] = useState('');
  const [startingHour, setStartingHour] = useState('');
  const [endingHour, setEndingHour] = useState('');
  const [planId, setPlanId] = useState(null); // 수정 모드인 경우 일정 ID를 저장할 상태

  useEffect(() => {
    fetchEventData();
    
  }, [clickedDate]);
  

  const fetchEventData = async () => {
    
    if (clickedDate) {
      const eventDate = new Date(clickedDate.year, clickedDate.month - 1, clickedDate.date + 1 );
      const formattedDate = eventDate.toISOString().split('T')[0];
      const response = await authAxios.get(`/plan/date/${formattedDate}`, { headers: headers });
      const eventData = response.data;
      console.log(eventData.id);
      setTitle(eventData.title);
      setColor(eventData.color);
      setSummary(eventData.summary);
      setStartingHour(eventData.startingHour);
      setEndingHour(eventData.endingHour);
      setPlanId(eventData.id);
      onEventChange(); // 상위 컴포넌트로 변경된 상태 전달

    } else {
      setTitle('');
      setColor('');
      setSummary('');
      setStartingHour('');
      setEndingHour('');
      setPlanId(null);
    }
  };


  const handleSave = () => {
    // 클릭된 날짜로부터 Date 객체 생성
    const eventDate = new Date(clickedDate.year, clickedDate.month - 1, clickedDate.date + 1);

    // ISO 8601 형식으로 변환 (YYYY-MM-DD)
    const formattedDate = eventDate.toISOString().split('T')[0];

    // color 상태가 비어 있는 경우 #FF8888으로 설정
    const selectedColor = color || '#FF8888';

    const eventData = {
      id : planId,
      title: title,
      color: selectedColor,
      date: formattedDate, // YYYY-MM-DD 형식으로 조합
      startingHour: startingHour,
      endingHour: endingHour,
      summary: summary,
    };

    // planId 값에 따라 일정을 추가하거나 수정합니다.
    const url = planId ? `/plan/${planId}` : '/plan';
    const method = planId ? 'PUT' : 'POST';

    authAxios({
      method: method,
      url: url,
      data: eventData,
      headers: headers,
    })
    .then(response => {
      console.log(eventData);
      console.log('캘린더 일정이 성공적으로 저장되었습니다.');
      onClose(); // 저장 후 팝업 닫기
      onEventChange(); // 상위 컴포넌트로 변경된 상태 전달
    })
    .catch(error => {
      console.error('캘린더 일정 저장에 실패했습니다.', error);
    });
  };

  const handleDelete = () => {
    if (!planId) {
      return; // planId가 없으면 삭제할 이벤트가 없으므로 아무 작업도 수행하지 않습니다.
    }

    const confirmed = window.confirm('정말로 이 일정을 삭제하시겠습니까?');
    if (!confirmed) {
      return; // 사용자가 취소하면 아무 작업도 수행하지 않습니다.
    }

    authAxios.delete(`/plan/${planId}`, { headers: headers })
      .then(response => {
        console.log('캘린더 일정이 성공적으로 삭제되었습니다.');
        // setUserSelectedColor(''); // 삭제 후 색상 값을 초기화하여 뷰에 반영
        onClose();
        onEventChange(); // 상위 컴포넌트로 변경된 상태 전달
      })
      .catch(error => {
        console.error('캘린더 일정 삭제에 실패했습니다.', error);
      });
  };

  const setColors = (e) => {
    setColor(e.target.id);
  };

  return (
    <div id="caledar_popup">
      <div className="popup_content">
        <span className="popup_close" onClick={onClose}><img src={popupClose} alt='popupClose'/></span> 
        <div className='popup_title'>
          <label className='popup_label' htmlFor="title">제목</label>
          <input type="text" id="title" value={title || ''} onChange={(e) => setTitle(e.target.value)} placeholder='제목을 입력하세요.' />
        </div>

        <div className='popupDateAndTime'>
          <div className='popup_cilckDate'>
            <p>날짜</p>
            <p>{clickedDate.year}. {clickedDate.month}. {clickedDate.date}</p>
          </div>
          
          <div className='popup_Time'>
          <p className='popup_label'>시간</p>
            <div className='popup_startTime'>
              <label htmlFor='startingHour'>시작 시간을 정해주세요</label>
              <input type='time' id='startingHour' value={startingHour || ''} onChange={(e) => setStartingHour(e.target.value)}/>
            </div>

            <div className='popup_endTime'>
              <label htmlFor='endingHour'>끝나는 시간을 정해주세요</label>
              <input type='time' id='endingHour' value={endingHour || ''} onChange={(e) => setEndingHour(e.target.value)}/>
            </div>
          </div>
        </div>

        <div className='popup_color'>
          <label className='popup_label' htmlFor="color">색상</label><span>{color}</span>
          <ul>
            {colors.map(((col, key) => {
              return (<li key={key} id={`${col}`} style={{ background: `${col}` }} onClick={e => { setColors(e) }}></li>)
            }))
            }
          </ul>
        </div>

        <div className='popup_summary'>
          <label className='popup_label' htmlFor="summary">메모</label>
          <input type="text" id="summary" value={summary || ''} onChange={(e) => setSummary(e.target.value)} placeholder='메모를 입력해주세요.' />
        </div>
        
        <div className='popup_saveAndUpdate'>
          <button onClick={handleSave}>{planId ? '수정' : '저장'}</button>
          {planId && <button onClick={handleDelete}>삭제</button>}
          {/* &times; */}
        </div>

      </div>
    </div>
  );
};

export default PopupEvent;