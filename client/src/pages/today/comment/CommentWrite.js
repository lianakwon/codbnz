import authAxios from "../../../interceptors";
import { useContext, useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { HttpHeadersContext } from "../../../HttpHeadersProvider";
import "../../../assets/todayComment.scss";

function CommentWrite(props) {
  const { headers } = useContext(HttpHeadersContext);
  const { todayId } = useParams();

  const id = localStorage.getItem("id");
  const writerName = localStorage.getItem("loginNickname");

  const [today, setToday] = useState({});
  const [profilePictureFileName, setProfilePictureFileName] = useState(""); // 프로필 사진 파일명 상태 추가
  const [content, setContent] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    fetchTodayData();
  }, []);

  const fetchProfilePicture = async () => {
    try {
      const response = await authAxios.get(
        `/today/${todayId}/comment/profileimg`,
        {
          headers,
        }
      );
      return response.data; // 프로필 이미지 파일명만을 반환하도록 수정
    } catch (error) {
      console.error("Error fetching profile picture:", error);
      return null;
    }
  };

  const fetchTodayData = async () => {
    try {
      const responseToday = await authAxios.get(`/today/${todayId}`, {
        headers,
      });
      const profilePictureFileName = await fetchProfilePicture(); // 프로필 이미지 파일명 가져오기
      setToday({ ...responseToday.data });
      setProfilePictureFileName(profilePictureFileName); // 프로필 이미지 파일명 상태 설정
    } catch (error) {
      console.error("Error fetching today data:", error);
    }
  };

  const changeContent = (event) => {
    setContent(event.target.value);
  };

  const createComment = async () => {
    const req = {
      content: content,
    };

    try {
      const response = await authAxios.post(
        `/today/${todayId}/comment/write`,
        req,
        { headers }
      );
      console.log("[CommentWrite.js] createComment() success :D");
      console.log(response.data);
      alert("댓글을 성공적으로 등록했습니다 :D");
      navigate(0); // 페이지 새로고침
    } catch (err) {
      console.log("[CommentWrite.js] createComment() error :<");
      console.log(err);
    }
  };

  return (
    <>
      {/* 상단 영역 (프로필 이미지, 댓글 작성자) */}
      <div className="commentwrite">
        <div className="col-1">
          <img
            src={`http://localhost:3000/profile/${profilePictureFileName}`}
            alt="프로필 이미지"
            className="profile-img"
            style={{
              height: "58px",
              width: "58px",
            }}
          />
        </div>
        <div className="col-7">
          <div className="comment_id_text">
            <span className="comment-id">{writerName}</span>
            <br />
            <textarea
              className="col-10"
              rows="1"
              value={content}
              onChange={changeContent}
            ></textarea>
          </div>
        </div>
      </div>
      {/* 하단 영역 (댓글 내용) */}
      <div className="my-3 d-flex justify-content-center">
        <div className="col-2 my-1 d-flex justify-content-end">
          <button className="ndary" onClick={createComment}>
            <i className="fas fa-comment-dots"></i> 댓글 추가
          </button>
        </div>
      </div>
      <br />
      <br />
    </>
  );
}

export default CommentWrite;
