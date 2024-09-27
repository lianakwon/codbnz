import authAxios from "../../../interceptors";
import React, { useRef } from "react";
import { useContext, useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { AuthContext } from "../../../AuthProvider";
import { HttpHeadersContext } from "../../../HttpHeadersProvider";
import heart from "../../../assets/icons/heart_selected_r.png";
import "../../../assets/todayComment.scss";

/* 댓글 컴포넌트 */
function Comment(props) {
  const { auth, setAuth } = useContext(AuthContext);
  const { headers, setHeaders } = useContext(HttpHeadersContext);

  const page = props.page;
  const [today, setToday] = useState({}); // 빈 객체로 초기화

  const comment = props.obj;
  const commentId = comment.commentId;
  const { todayId } = useParams(); // todayId, 파라미터 가져오기

  const [show, setShow] = useState(false);

  const [content, setContent] = useState(comment.content);
  const changeContent = (event) => {
    setContent(event.target.value);
  };

  // useEffect 훅 추가: 컴포넌트가 마운트될 때 fetchTodayData 함수를 호출
  useEffect(() => {
    fetchTodayData();
  }, []);

  // fetchTodayData 함수 추가: today 데이터를 가져와서 설정
  const fetchTodayData = async () => {
    try {
      const response = await authAxios.get(`/today/${todayId}`, { headers });
      setToday(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  /* 댓글 수정 */
  const updateComment = async () => {
    const req = {
      content: content,
    };

    await authAxios
      .patch(`/${todayId}/comment/update/${commentId}`, req, {
        headers: headers,
      })
      .then((resp) => {
        console.log("[Comment.js] updateComment() success :D");
        console.log(resp.data);

        alert("댓글을 성공적으로 수정했습니다 !");

        // 업데이트된 댓글 목록을 다시 불러오기
        props.getCommentList(page);
      })
      .catch((err) => {
        console.log("[Comment.js] updateComment() error :<");
        console.log(err);

        alert(err);
      });
    updateToggle();
  };

  /* 댓글 삭제 */
  const deleteComment = async () => {
    await authAxios
      .delete(`/today/${todayId}/comment/delete/${commentId}`, {
        headers: headers,
      })
      .then((resp) => {
        console.log("[TodayComment.js] deleteComment() success :D");
        console.log(resp.data);

        alert("답글을 성공적으로 삭제했습니다 :D");
        //삭제된 댓글 목록 다시 불러오기
        props.getCommentList(page);
      })
      .catch((err) => {
        console.log("[TodayComment.js] deleteComment() error :<");
        console.log(err);
      });
  };

  /*댓글 좋아요*/
  const handleLikeComment = async (commentId) => {
    try {
      const id = localStorage.getItem("id");
      console.log("id:", id); // id 값 콘솔에 출력

      const response = await authAxios.post(
        `/today/${todayId}/comment/${commentId}/like`,
        null,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
          },
        }
      );

      if (response.status === 200) {
        alert(response.data);
      }
      props.getCommentList(page);
    } catch (error) {
      console.error("추천에 실패했습니다:", error);
    }
  };

  function updateToggle() {
    setShow((show) => !show);
  }

  return (
    <>
      {/* 상단 영역 (프로필 이미지, 댓글 작성자, 댓글 작성시간) */}
      <div className="my-1 d-flex justify-content-center">
        <div className="profile_img">
          <img
            className="profile"
            src={`http://localhost:3000/profile/${comment.profilePictureFileName}`}
            alt={`${comment.commentWriterName}`}
          />
          <span className="comment-id">{comment.commentWriterName}</span>
          <span className="date">{comment.createdDate}</span>
          <span
            className="like"
            onClick={() => handleLikeComment(comment.commentId)}
          >
            <img src={heart} alt="Like" />
            {comment.likeCount}
          </span>
          {localStorage.getItem("id") == comment.commentWriterId && (
            <>
              <button
                className="btn btn-outline-secondary"
                onClick={updateToggle}
              >
                <i className="fas fa-edit"></i> 수정
              </button>{" "}
              &nbsp;
              <button
                className="btn btn-outline-danger"
                onClick={deleteComment}
              >
                <i className="fas fa-trash-alt"></i> 삭제
              </button>
            </>
          )}
        </div>
      </div>

      {show ? (
        <>
          {/* 하단 영역 (댓글 내용 + 댓글 내용 편집 창) */}
          <div className="my-3 d-flex justify-content-center">
            <textarea
              className="col-10"
              rows="5"
              value={content}
              onChange={changeContent}
            ></textarea>
          </div>
          <div className="my-1 d-flex justify-content-center">
            <button className="btn btn-dark" onClick={updateComment}>
              <i className="fas fa-edit"></i>수정 완료
            </button>
          </div>
        </>
      ) : (
        <>
          {/* 하단 영역 (댓글 내용) */}
          <div className="my-3 d-flex justify-content-center">
            <div className="col-10 comment">{content}</div>
          </div>
        </>
      )}
    </>
  );
}

export default Comment;
