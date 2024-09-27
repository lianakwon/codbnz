import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import Pagination from "react-js-pagination";
import authAxios from "../../../interceptors";

import heart from "../../../assets/icons/heart_selected_r.png";
import alert from "../../../assets/icons/alert.png";
import question from "../../../assets/icons/question_g.png";

import "../../../assets/today.scss";

function getAnswerStatusText(answerStatus) {
  switch (answerStatus) {
    case "IN_PROGRESS":
      return "궁금해요";
    case "COMPLETED":
      return "해결완료!";
    case "NOT_APPLICABLE":
      return "잡담";
    default:
      return "";
  }
}

function Today() {
  // ★ 공통사항 ★ useNavigate
  const navigate = useNavigate();

  // ★ 공통사항 ★ 페이지 제목 클릭 시 이전페이지로
  const clickTitle = () => {
    navigate(-1);
  };

  const [today, setToday] = useState([]);

  // 검색용 Hook
  const [choiceVal, setChoiceVal] = useState("title");
  const [choiceName, setChoiceName] = useState("제목");
  const [searchVal, setSearchVal] = useState("");

  // Paging
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalCnt, setTotalCnt] = useState(0);

  // 게시글 전체 조회
  const getToday = async (page) => {
    try {
      const response = await authAxios.get("/today/list", {
        params: { page: page - 1 },
      });

      console.log("[Today.js] Today data:", response.data);

      setToday(response.data.content);
      setPageSize(response.data.pageSize);
      setTotalPages(response.data.totalPages);
      setTotalCnt(response.data.totalElements);
    } catch (error) {
      console.error("[Today.js] useEffect() error :<", error);
    }
  };

  // 게시글 검색
  const search = async (e) => {
    e.preventDefault();
    try {
      const response = await authAxios.get("/today/search", {
        params: {
          page: page - 1,
          title: choiceVal === "title" ? searchVal : "",
          content: choiceVal === "content" ? searchVal : "",
          writerName: choiceVal === "writer" ? searchVal : "",
        },
      });
      if (searchVal === "") {
        return; // 검색어가 비어 있는 경우 검색하지 않음
      }
      // 검색 결과가 있는 경우
      setToday(response.data.content);
      setTotalCnt(response.data.totalElements);
    } catch (error) {
      console.error("[Today.js searchBtn()] error :<", error);
    }
  };

  // 첫 로딩 시, 한 페이지만 가져옴
  useEffect(() => {
    getToday(1);
  }, []);

  // 검색 조건 저장
  const changeChoice = (e) => {
    setChoiceVal(e.target.id);
    setChoiceName(e.target.className);
  };

  const clickSelect = () => {
    const select = document.querySelector("#today .form .select");
    if (!select.classList.contains("on")) select.classList.toggle("on");
    else select.classList.remove("on");
  };
  useEffect(() => {
    clickSelect();
  }, [choiceVal]);

  const changeSearch = (e) => {
    setSearchVal(e.target.value);
  };

  const changePage = (page) => {
    setPage(page);
    getToday(page);
  };

  return (
    <>
      <div id="today" className="inner_m">
        <div className="title">
          <h2 onClick={clickTitle} className="link_back">
            빈즈투데이
          </h2>
          <Link className="add" to="/today/add">
            글쓰기
          </Link>
        </div>

        <div className="list">
          <ul>
            {today &&
              today.map((item, key) => (
                <li key={key}>
                  <Link to={`/today/${item.todayId}`}>
                    {item.thumbnailPath ? (
                      <div className="thum">
                        <img
                          src={`http://localhost:3000/files/${item.thumbnailPath}`}
                          alt={`${item.title}`}
                        />
                      </div>
                    ) : (
                      <div className="thum none"></div>
                    )}
                    <div className="text">
                      <div className="decs">
                        <h4>{item.title}</h4>
                        <span>
                          {item.content.length > 20
                            ? item.content.slice(0, 40) + "..."
                            : item.content}
                        </span>
                        <p>@{item.writerName}</p>
                      </div>
                      <ul className="icon">
                        <li
                          className={
                            item.answerStatus === "IN_PROGRESS"
                              ? "commentIng"
                              : item.answerStatus === "COMPLETED"
                              ? "commentDone"
                              : "chatting"
                          }
                        >
                          {getAnswerStatusText(item.answerStatus)}
                        </li>
                        <li className="quest">
                          <img src={question} alt="question" />
                          <p>{item.questionCount}</p>
                        </li>
                        <li className="likes">
                          <img src={heart} alt="heartIcon" />
                          <p>{item.likeCount}</p>
                        </li>
                        <li className="cmnts">
                          <img src={alert} alt="comment" />
                          <p>{item.commentCount}</p>
                        </li>
                        <li className="views">
                          <p>조회수 {item.viewCount}</p>
                        </li>
                      </ul>
                    </div>
                  </Link>
                </li>
              ))}
          </ul>
        </div>

        <div className="page">
          <Pagination
            prevPageText={"‹"}
            nextPageText={"›"}
            onChange={changePage}
            activePage={page}
            itemsCountPerPage={pageSize}
            totalItemsCount={totalCnt}
            pageRangeDisplayed={totalPages}
          />
        </div>

        <div className="form">
          <form onSubmit={search}>
            <div className="select on" onClick={clickSelect}>
              <h6>{choiceName}</h6>
              <ul>
                <li id="title" className="제목" onClick={changeChoice}>
                  제목
                </li>
                <li id="content" className="내용" onClick={changeChoice}>
                  내용
                </li>
                <li id="writer" className="작성자" onClick={changeChoice}>
                  작성자
                </li>
              </ul>
            </div>
            <div className="search">
              <input
                type="text"
                className="form-control"
                placeholder="검색어"
                value={searchVal}
                onChange={changeSearch}
              />
              <button type="submit" disabled={!searchVal}>
                찾기
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* <div><div id="todayContainer" className="inner_m"><h2 className="link_back">빈즈투데이</h2><Link className="todayCreate" to="/todaywrite">글쓰기</Link>
        <div className="today_list">
          {today &&
            today.map((item, idx) => (
              <Link to={`/todaydetail/${item.todayId}`} key={idx}>
                <li className="contentBox">
                  {item.thumbnailPath ? (
                    <div className="thumbnail_img_box">
                      <img
                        src={`http://localhost:3000/files/${item.thumbnailPath}`}
                        alt="Thumbnail"
                        className="thumbnail"
                      />
                    </div>
                  ) : (
                    <div className="thumbnail_img_none"></div>
                  )}
                  <div className="text_and_icons">
                    <div className="today_text_wrap">
                      <h4>{item.title}</h4>
                      <p className="content">
                        {item.content.length > 20
                          ? item.content.slice(0, 40) + "..."
                          : item.content}
                      </p>
                      <p className="nickName">{item.writerName}</p>
                    </div>
                    <ul>
                      <li
                        className={
                          item.answerStatus === "IN_PROGRESS"
                            ? "commentIng"
                            : item.answerStatus === "COMPLETED"
                              ? "commentDone"
                              : "chatting"
                        }
                      >
                        {getAnswerStatusText(item.answerStatus)}
                      </li>
                      <li>
                        <span>
                          <img src={question} alt="question" />
                        </span>
                        <p>{item.questionCount}</p>
                      </li>
                      <li>
                        <span>
                          <img src={heart} alt="heartIcon" />
                        </span>
                        <p>{item.likeCount}</p>
                      </li>
                      <li>
                        <span>
                          <img src={alert} alt="comment" />
                        </span>
                        <p>{item.commentCount}</p>
                      </li>

                      <li>
                        <span>
                          <img src={view} alt="viewcount" />
                        </span>
                        <p>{item.viewCount}</p>
                      </li>
                    </ul>
                  </div>
                </li>
              </Link>
            ))}</div>
        <Pagination className="pagination" prevPageText={"‹"} nextPageText={"›"} onChange={changePage} activePage={page} itemsCountPerPage={pageSize} totalItemsCount={totalCnt} pageRangeDisplayed={totalPages} />

        <table className="search">
          <tbody>
            <tr>
              <td>
                <select className="custom-select" value={choiceVal} onChange={changeChoice} >
                  <option>검색 옵션</option>
                  <option value="title">제목</option>
                  <option value="content">내용</option>
                  <option value="writer">작성자</option>
                </select>
              </td>
              <td><input type="text" className="form-control" placeholder="검색어" value={searchVal} onChange={changeSearch} /></td>
              <td><button type="button" className="btn btn-outline-secondary" onClick={search}><i className="fas fa-search"></i>검색</button></td>
            </tr>
          </tbody>
        </table>
        <div className="my-5 d-flex justify-content-center"></div>
      </div>
      </div> */}
    </>
  );
}

export default Today;
