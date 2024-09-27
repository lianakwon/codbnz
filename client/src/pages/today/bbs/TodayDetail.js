import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import authAxios from "../../../interceptors";
import { AuthContext } from "../../../AuthProvider";
import { HttpHeadersContext } from "../../../HttpHeadersProvider";

import CommentWrite from "../comment/CommentWrite";
import CommentList from "../comment/CommentList";
import FileDisplay from "../file/FileDisplay";
import heart from "../../../assets/icons/heart_selected_r.png";
import question from "../../../assets/icons/question_g.png";

import "../../../assets/todaypage.scss";

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

function TodayDetail() {
  const { headers, setHeaders } = useContext(HttpHeadersContext);
  const { auth, setAuth } = useContext(AuthContext);
  const [today, setToday] = useState({});
  const { todayId } = useParams(); // 파라미터 가져오기
  const navigate = useNavigate();

  const [showOptions, setShowOptions] = useState(false);

  const toggleOptions = () => {
    setShowOptions(!showOptions);
  };

  const handleLike = async () => {
    try {
      const id = localStorage.getItem("id");
      console.log("loginUsername:", id); // id 값 콘솔에 출력

      const response = await authAxios.post(`/today/${todayId}/like`, {
        id: id,
      });

      if (response.status === 200) {
        alert(response.data);
        getTodayDetail();
      }
    } catch (error) {
      console.error("추천에 실패했습니다:", error);
    }
  };

  const handleQuestion = async () => {
    try {
      const loginUsername = localStorage.getItem("id");
      console.log("loginUsername:", loginUsername); // id 값 콘솔에 출력

      const response = await authAxios.post(`/today/${todayId}/question`, {
        id: loginUsername, // 로그인한 사용자의 ID 또는 토큰
      });

      if (response.status === 200) {
        alert(response.data);
        getTodayDetail();
      }
    } catch (error) {
      console.error("궁금해요 설정에 실패했습니다:", error);
    }
  };

  const getTodayDetail = async () => {
    try {
      const response = await authAxios.get(`/today/${todayId}`);

      console.log("[TodayDetail.js] getTodayDetail() success :D");
      console.log(response.data);

      setToday(response.data);
    } catch (error) {
      console.log("[TodayDetail.js] getTodayDetail() error :<");
      console.error(error);
    }
  };

  const deleteToday = async () => {
    try {
      const response = await authAxios.delete(`/today/${todayId}/delete`, {
        headers: headers,
      });

      console.log("[TodayDetail.js] deleteToday() success :D");
      console.log(response.data);

      if (response.status == 200) {
        alert("게시글을 성공적으로 삭제했습니다 :D");
        navigate("/today");
      }
    } catch (error) {
      console.log("[TodayDetail.js] deleteToday() error :<");
      console.error(error);
    }
  };

  useEffect(() => {
    // 컴포넌트가 렌더링될 때마다 localStorage의 토큰 값으로 headers를 업데이트
    setHeaders({
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    });
    getTodayDetail();
  }, []);

  const updateToday = {
    todayId: today.todayId,
    writerName: today.writerName,
    title: today.title,
    content: today.content,
    files: today.files,
  };

  const parentToday = {
    todayId: today.todayId,
    title: today.title,
  };

  return (
    <>
      <div id="today_detail" className="inner_m">
        <div className="black_box">
          <div className="cate"></div>
          <div className="status"></div>
          <div className="title"></div>
          <div className="cont"></div>
          <div className="thum"></div>
          <div className="comment">
            <span>댓글</span>
            <CommentList todayId={todayId} />
            {auth ? <CommentWrite todayId={todayId} /> : null}
          </div>
        </div>

        <div className="inner_box">
          <div className="cate">
            <span>분류</span>
            <h4 className="today_text">빈즈투데이</h4>
          </div>

          <div className="status">
            <span>상태</span>
            <h4
              className={
                today.answerStatus === "IN_PROGRESS"
                  ? "commentIng"
                  : today.answerStatus === "COMPLETED"
                  ? "commentDone"
                  : "chatting"
              }
            >
              {getAnswerStatusText(today.answerStatus)}
            </h4>
          </div>

          <div className="title">
            <span>제목</span>
            <h4 className="today_text">{today.title}</h4>
            <ul className="details">
              <li>{today.createdDate}</li>
              <li className="likes" onClick={handleLike}>
                <img src={heart} alt="Like" />
                {today.likeCount}
              </li>
              <li className="quest" onClick={handleQuestion}>
                <img src={question} alt="Question" />
                {today.questionCount}
              </li>
            </ul>
          </div>

          <div className="cont">
            <div className="cont_divide">
              <span>내용</span>
              {localStorage.getItem("id") === today.writerId && (
                <>
                  <div>
                    <Link
                      className="btn btn-outline-secondary"
                      to="/todayupdate"
                      state={{ today: updateToday }}
                    >
                      수정
                    </Link>{" "}
                    &nbsp;
                    <button
                      className="btn btn-outline-danger"
                      onClick={deleteToday}
                    >
                      삭제
                    </button>
                  </div>
                </>
              )}
            </div>

            <h4 className="detail_content">{today.content}</h4>
            <div className="writer">
              <img
                src={`http://localhost:3000/profile/${today.profilePictureFileName}`}
                alt="프로필 이미지"
                className="profile"
                onError={(e) => {
                  console.error("Image load error", e);
                  console.log("Image path:", `./files/${today.thumbnailPath}`);
                }}
              />
              <div className="profile_namemsg">
                <h4>{today.writerName}</h4>
                <div>{today.profileMSG}</div>
              </div>
            </div>
          </div>

          <div className="thum">
            <span>썸네일</span>
            <h4>
              {today.thumbnailPath ? (
                <>
                  <div className="thumbnail_img_box">
                    <img
                      src={`http://localhost:3000/files/${today.thumbnailPath}`}
                      alt="Thumbnail"
                      className="thumbnail"
                      onError={(e) => {
                        console.error(
                          "Image load error",
                          e,
                          ", Image path:",
                          `./files/${today.thumbnailPath}`
                        );
                      }}
                    />
                  </div>
                </>
              ) : (
                <div className="thumbnail_img_none">썸네일이 없습니다</div>
              )}
            </h4>
            {today.files ? (
              <>
                <span style={{ marginBottom: "20px" }}>첨부파일</span>
                <h4>
                  <div className="thumbnail_img_box">
                    <FileDisplay files={today.files} todayId={todayId} />
                  </div>
                </h4>
              </>
            ) : (
              <></>
            )}
          </div>

          <div className="comment">
            <span>댓글</span>
            <CommentList todayId={todayId} />
            {auth ? <CommentWrite todayId={todayId} /> : null}
          </div>
        </div>
      </div>
    </>
  );
}

export default TodayDetail;
