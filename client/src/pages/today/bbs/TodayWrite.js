import React, { useEffect, useState, useContext, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import authAxios from "../../../interceptors";
import { AuthContext } from "../../../AuthProvider";
import { HttpHeadersContext } from "../../../HttpHeadersProvider";
import "../../../assets/todaypage.scss";

function TodayWrite() {
  const { headers, setHeaders } = useContext(HttpHeadersContext);
  const { auth } = useContext(AuthContext);
  const navigate = useNavigate();

  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [files, setFiles] = useState([]);
  const [thumbnails, setThumbnails] = useState([]);
  const [answerStatus, setAnswerStatus] = useState("NOT_APPLICABLE");

  const fileInputRef = useRef(null);
  const thumbnailInputRef = useRef(null);

  const changeTitle = (event) => {
    setTitle(event.target.value);
  };

  const changeContent = (event) => {
    setContent(event.target.value);
  };

  const handleChangeFile = (event) => {
    const selectedFiles = Array.from(event.target.files);
    setFiles((prevFiles) => [
      ...prevFiles,
      ...selectedFiles.slice(0, 5 - prevFiles.length),
    ]);
  };

  const handleChangeThumbnail = (event) => {
    const thumbnailFile = event.target.files[0];
    setThumbnails(thumbnailFile ? [thumbnailFile] : []);
  };

  const handleRemoveFile = (index) => {
    setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
  };

  const handleRemoveThumbnail = () => {
    setThumbnails([]);
    if (thumbnailInputRef.current) {
      thumbnailInputRef.current.value = "";
    }
  };

  const handleChangeStatus = (event) => {
    setAnswerStatus(event.target.value);
  };

  const fileUpload = async (todayId) => {
    if (files.length === 0) return;

    try {
      const fd = new FormData();
      files.forEach((file) => {
        fd.append("file", file, file.name);
      });

      await authAxios.post(`/today/${todayId}/file/upload`, fd, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      alert("파일 업로드 성공 :D");
    } catch (error) {
      console.error("[TodayWrite.js] fileUpload() error :<", error);
      alert("파일 업로드 실패 :(");
    }
  };

  const thumbnailUpload = async (todayId) => {
    if (thumbnails.length === 0) return;

    try {
      const fd = new FormData();
      fd.append("thumbnail", thumbnails[0]);

      await authAxios.post(`/today/${todayId}/file/thumbnail/upload`, fd, {
        headers: headers,
      });

      alert("썸네일 업로드 성공 :D");
    } catch (error) {
      console.error("[TodayWrite.js] thumbnailUpload() error :<", error);
      alert("썸네일 업로드 실패 :(");
    }
  };

  const createToday = async () => {
    if (!title || !content) {
      alert("제목과 내용은 필수사항 입니다");
      return;
    }

    const req = {
      title: title,
      content: content,
      answerStatus: answerStatus,
    };

    try {
      const response = await authAxios.post("/today/write", req, {
        headers: headers,
      });

      const todayId = response.data.todayId;

      await Promise.all([fileUpload(todayId), thumbnailUpload(todayId)]);

      alert("새로운 게시글을 성공적으로 등록했습니다 :D");
      navigate(`/today/${todayId}`);
    } catch (error) {
      console.error("[TodayWrite.js] createToday() error :<", error);
    }
  };

  useEffect(() => {
    setHeaders({
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    });

    if (!auth) {
      alert("로그인 한 사용자만 게시글을 작성할 수 있습니다 !");
      navigate(-1);
    }
  }, [auth, navigate, setHeaders]);

  return (
    <>
      <div id="today_detail" className="inner_m">
        <div className="black_box">
          <div className="cate"></div>
          <div className="status"></div>
          <div className="title"></div>
          <div className="cont"></div>
          <div className="thum"></div>
        </div>

        <div className="inner_box">
          <div className="cate">
            <span>분류</span>
            <h4>빈즈투데이</h4>
          </div>

          <div className="status">
            <span>상태</span>
            <h4>
              <select
                className="form-control"
                value={answerStatus}
                onChange={handleChangeStatus}
              >
                <option value="IN_PROGRESS">궁금해요</option>
                <option value="COMPLETED">해결됬어요!</option>
                <option value="NOT_APPLICABLE">잡담</option>
              </select>
            </h4>
          </div>

          <div className="title">
            <span>제목</span>
            <h4>
              <input
                type="text"
                className="form-control"
                value={title}
                onChange={changeTitle}
                placeholder="제목을 입력하세요"
              />
            </h4>
          </div>

          <div className="cont">
            <span>내용</span>
            <h4>
              <textarea
                name="today_content"
                value={content}
                onChange={changeContent}
                placeholder="게시글 내용을 입력하세요"
              ></textarea>
            </h4>
          </div>

          <div className="thum">
            <span>썸네일</span>
            <h4>파일을 등록해주세요</h4>
            {thumbnails.map((thumbnail, index) => (
              <div
                key={index}
                style={{ display: "flex", alignItems: "center" }}
              >
                <img
                  src={URL.createObjectURL(thumbnail)}
                  alt={`Thumbnail ${index}`}
                  style={{ width: "280px", height: "280px" }}
                />
                <button
                  className="delete-button"
                  type="button"
                  onClick={handleRemoveThumbnail}
                  style={{
                    marginLeft: "10px",
                    background: "transparent",
                    border: "none",
                    cursor: "pointer",
                    color: "red",
                    fontSize: "22px",
                  }}
                >
                  x
                </button>
              </div>
            ))}
            {thumbnails.length === 0 && (
              <span>
                최대 파일 크기 : 5MB <br />
                이미지 사이즈 : 420 * 300 (px)
                <br />
                확장자 : JPG, JPEG, PNG, GIF, TIFF, BMP, EPS, SVG
              </span>
            )}
            <input
              type="file"
              name="thumbnail"
              ref={thumbnailInputRef}
              onChange={handleChangeThumbnail}
            />
          </div>

          <div className="file_text">첨부파일</div>
          <h4>파일을 불러와주세요</h4>
          {files.map((file, index) => (
            <div key={index} style={{ display: "flex", alignItems: "center" }}>
              <p>
                <strong>FileName:</strong> {file.name}
              </p>
              <button
                className="delete-button"
                type="button"
                onClick={() => handleRemoveFile(index)}
                style={{
                  marginLeft: "10px",
                  background: "transparent",
                  border: "none",
                  cursor: "pointer",
                  color: "red",
                  fontSize: "22px",
                }}
              >
                x
              </button>
            </div>
          ))}
          {files.length < 5 && (
            <div>
              <input
                type="file"
                name="file"
                ref={fileInputRef}
                onChange={handleChangeFile}
                multiple
              />
            </div>
          )}
          <button type="submit" className="create_link" onClick={createToday}>
            등록하기
          </button>
        </div>
      </div>
    </>
  );
}

export default TodayWrite;
