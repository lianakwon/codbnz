import React, { useContext, useEffect, useState, useRef } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import { HttpHeadersContext } from "../../HttpHeadersProvider";
import { AuthContext } from "../../AuthProvider";
// import { MateSelectArea } from "../../assets/MateSelectArea.js";

import "../../assets/createLayout.scss";

function MateForm() {
  const { id } = useParams();
  const { auth, setAuth } = useContext(AuthContext);
  const { headers, setHeaders } = useContext(HttpHeadersContext);
  const isEditMode = !!id;

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [selectedGoal, setSelectedGoal] = useState(""); // 목표
  const [selectedLang, setSelectedLang] = useState(""); // 언어
  const [selectedSpace, setSelectedSpace] = useState("");
  const [selectedLocas, setSelectedLocas] = useState(""); // 단일 값으로 설정
  const [selectedParts, setSelectedParts] = useState([]);
  const [selectedProgram, setSelectedProgram] = useState(""); // 단일 값으로 설정
  const [selectedRecruit, setSelectedRecruit] = useState(false); // 모집 여부

  const blackContentRef = useRef(null);
  const blackCommentRef = useRef(null);

  useEffect(() => {
    const whiteContentElement = document.getElementById("white_content");
    const whiteCommentElement = document.getElementById("white_comment");
    const blackContentElement = blackContentRef.current;
    const blackCommentElement = blackCommentRef.current;

    if (whiteContentElement && whiteCommentElement) {
      const whiteContentHeight = whiteContentElement.offsetHeight;
      const whiteCommentHeight = whiteCommentElement.offsetHeight;

      if (blackContentElement && blackCommentElement) {
        blackContentElement.style.height = `${whiteContentHeight}px`;
        blackCommentElement.style.height = `${whiteCommentHeight}px`;
      }
    }
  }, []);

  const navigate = useNavigate();

  const changeTitle = (event) => {
    setTitle(event.target.value);
  };

  const changeContent = (event) => {
    setContent(event.target.value);
  };

  const handleCheckboxChange = (e) => {
    const { name, value, checked } = e.target;
    const updateSelection = (prevSelected) =>
      checked
        ? [...prevSelected, value]
        : prevSelected.filter((field) => field !== value);

    switch (name) {
      case "selectedParts":
        setSelectedParts(updateSelection(selectedParts));
        break;
      default:
        break;
    }
  };

  const handleLocasChange = (e) => {
    setSelectedLocas(e.target.value);
  };

  useEffect(() => {
    setHeaders({
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    });

    if (isEditMode) {
      axios
        .get(`http://localhost:8080/mate/detail/${id}`, {
          headers: headers,
        })
        .then((response) => {
          const mate = response.data;
          setTitle(mate.title);
          setContent(mate.content);
          // Tag
          if (mate.tag) {
            setSelectedGoal(mate.tag.goal);
            setSelectedLang(mate.tag.langs);
            setSelectedSpace(mate.tag.space);
            setSelectedLocas(mate.tag.locas);
            setSelectedParts(mate.tag.parts);
            setSelectedProgram(mate.tag.programs);
            setSelectedRecruit(mate.tag.recruit);
          }
        })
        .catch((error) => {
          console.error("게시글 데이터를 불러오는데 실패했습니다.", error);
        });
    }
  }, [auth, navigate, setHeaders, id, isEditMode]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    const res = {
      title,
      content,
      create_date: new Date().toISOString(),
      update_date: new Date().toISOString(),
      tag: {
        recruit: selectedRecruit,
        goal: selectedGoal,
        locas: [selectedLocas],
        parts: selectedParts,
        langs: [selectedLang],
        programs: [selectedProgram],
        space: selectedSpace,
      },
    };

    try {
      let response;
      if (isEditMode) {
        response = await axios.put(
          `http://localhost:8080/mate/update/${id}`,
          res,
          {
            headers: headers,
          }
        );
        alert("수정완료!");
      } else {
        response = await axios.post("http://localhost:8080/mate/create", res, {
          headers: headers,
        });
        alert("등록완료!");
      }
      console.log("요청 성공:", response.data);
      navigate(`/mate/detail/${response.data.id}`);
    } catch (error) {
      console.error("게시글 업로드에 실패했습니다!", error);
      if (error.response) {
        console.log("리스폰데이터 로드 실패:", error.response.data);
      }
    }
  };

  return (
    <>
      <div className="create_container">
        <div className="create_flex_column">
          <div className="create_categoryname_divide">
            <div className="create_category list_style">
              <h4 className="create_h4">분류</h4>
              빈즈메이트
            </div>
            <div className="create_categoryname_divide_right"></div>
          </div>
          <div className="create_title_select">
            <div className="create_title list_style">
              <h4 className="create_h4">제목</h4>
              <input
                type="text"
                className="form-control"
                value={title}
                onChange={changeTitle}
                placeholder="제목을 입력하세요"
              />
            </div>
          </div>
        </div>
        <div className="create_flex_row" id="white_content">
          <div className="create_left_wrap list_style">
            <h4 className="create_h4">내용</h4>
            <textarea
              name="mate_content"
              value={content}
              onChange={changeContent}
              placeholder="게시글 내용을 입력하세요"
            ></textarea>
          </div>
          <div className="create_right_wrap list_style" id="white_comment">
            <div>
              <h4>목적</h4>
              <div className="mate_space">
                <label className={selectedGoal === "STUDY" ? "selected" : ""}>
                  <input
                    type="radio"
                    name="selectedGoal"
                    value="STUDY"
                    checked={selectedGoal === "STUDY"}
                    onChange={(e) => setSelectedGoal(e.target.value)}
                  />
                  스터디
                </label>
                <label className={selectedGoal === "PROJECT" ? "selected" : ""}>
                  <input
                    type="radio"
                    name="selectedGoal"
                    value="PROJECT"
                    checked={selectedGoal === "PROJECT"}
                    onChange={(e) => setSelectedGoal(e.target.value)}
                  />
                  프로젝트
                </label>
              </div>
              <h4>온라인/오프라인</h4>
              <div className="mate_space">
                <label className={selectedSpace === "ONLINE" ? "selected" : ""}>
                  <input
                    type="radio"
                    name="selectedSpace"
                    value="ONLINE"
                    checked={selectedSpace === "ONLINE"}
                    onChange={(e) => setSelectedSpace(e.target.value)}
                  />
                  온라인
                </label>
                <label
                  className={selectedSpace === "OFFLINE" ? "selected" : ""}
                >
                  <input
                    type="radio"
                    name="selectedSpace"
                    value="OFFLINE"
                    checked={selectedSpace === "OFFLINE"}
                    onChange={(e) => setSelectedSpace(e.target.value)}
                  />
                  오프라인
                </label>
              </div>
              <h4 className="create_h4">모집분야</h4>
              <ul className="tag_flex_wrap selected-options">
                {[
                  "UI·UX 디자이너",
                  "프로덕트 디자이너",
                  "프론트엔드",
                  "백엔드",
                  "웹기획자",
                  "안드로이드 개발자",
                  "ios개발자",
                ].map((part) => (
                  <li
                    key={part}
                    className={selectedParts.includes(part) ? "selected" : ""}
                  >
                    <input
                      type="checkbox"
                      id={part}
                      name="selectedParts"
                      value={part}
                      onChange={handleCheckboxChange}
                    />
                    <label htmlFor={part}>{part}</label>
                  </li>
                ))}
              </ul>
              <h4 className="create_h4">사용 프로그램 / 언어</h4>
              <div className="create_select_wrap">
                <div className="mate_select_wrap">
                  <select
                    id="program"
                    value={selectedProgram}
                    onChange={(e) => setSelectedProgram(e.target.value)}
                  >
                    <option value="">프로그램</option>
                    {["Photoshop", "Illustrator", "MySQL", "MariaDB"].map(
                      (program) => (
                        <option key={program} value={program}>
                          {program}
                        </option>
                      )
                    )}
                  </select>
                  <select
                    id="lang"
                    value={selectedLang}
                    onChange={(e) => setSelectedLang(e.target.value)}
                  >
                    <option value="">언어</option>
                    {["JavaScript", "PHP", "Java", "Python", "C#", "C++"].map(
                      (lang) => (
                        <option key={lang} value={lang}>
                          {lang}
                        </option>
                      )
                    )}
                  </select>
                </div>
              </div>
              {/* space값이 '오프라인'이라면 지역보여주기 */}
              <div>
                <h4 className="create_h4">지역</h4>
                <div>
                  <div className="create_select_wrap">
                    <select
                      name="locas"
                      id="locas"
                      value={selectedLocas}
                      onChange={handleLocasChange}
                    >
                      <option value="">지역을 선택하세요</option>
                      {[
                        "서울",
                        "부산",
                        "대구",
                        "인천",
                        "광주",
                        "대전",
                        "울산",
                        "세종",
                        "경기",
                        "강원",
                        "충북",
                        "충남",
                        "전북",
                        "전남",
                        "경북",
                        "경남",
                        "제주",
                      ].map((loca) => (
                        <option key={loca} value={loca}>
                          {loca}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>
              <button
                type="submit"
                className="create_link"
                onClick={handleSubmit}
              >
                {isEditMode ? "수정하기" : "등록하기"}
              </button>
            </div>
          </div>
        </div>
        <div className="create_black_box">
          <div className="create_category create_black_cont"></div>
          <div className="create_title create_black_cont"></div>
          <div
            className="create_contents create_black_cont"
            ref={blackContentRef}
          ></div>
          <div
            className="create_right_wrap create_black_cont"
            ref={blackCommentRef}
          ></div>
        </div>
      </div>
    </>
  );
}

export default MateForm;
