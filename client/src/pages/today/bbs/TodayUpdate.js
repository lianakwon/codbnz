import authAxios from "../../../interceptors";
import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useContext, useState } from "react";
import { AuthContext } from "../../../AuthProvider";
import { HttpHeadersContext } from "../../../HttpHeadersProvider";
import "../../../assets/todaypage.scss";

function TodayUpdate() {
  const { headers, setHeaders } = useContext(HttpHeadersContext);
  const { auth, setAuth } = useContext(AuthContext);
  const navigate = useNavigate();

  const location = useLocation();
  const { today } = location.state;

  const todayId = today.todayId;
  const [title, setTitle] = useState(today.title);
  const [content, setContent] = useState(today.content);
  const [files, setFiles] = useState([]);
  const [thumbnails, setThumbnails] = useState(today.thumbnails || []);
  const [severFiles, setSeverFiles] = useState(today.files || []);
  const [answerStatus, setAnswerStatus] = useState("NOT_APPLICABLE");

  const changeTitle = (event) => {
    setTitle(event.target.value);
  };

  const changeContent = (event) => {
    setContent(event.target.value);
  };

  const handleChangeFile = (event) => {
    // 총 5개까지만 허용
    const selectedFiles = Array.from(event.target.files).slice(0, 5);
    setFiles((prevFiles) => [...prevFiles, ...selectedFiles]);
  };

  const handleChangeThumbnail = (event) => {
    // 썸네일은 1개만 허용
    const thumbnailFile = event.target.files[0];
    setThumbnails(thumbnailFile ? [thumbnailFile] : []);
  };
  const handleRemoveThumbnail = () => {
    setThumbnails([]);
  };

  const handleRemoveFile = (index) => {
    setFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
  };

  const handleRemoveSeverFile = (index, todayId, fileId) => {
    setSeverFiles((prevFiles) => prevFiles.filter((_, i) => i !== index));
    fileDelete(todayId, fileId);
  };

  useEffect(() => {
    setHeaders({
      Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
    });
  }, []);
  const handleChangeStatus = (event) => {
    const answerStatus = event.target.value;
    console.log("선택된 답변 상태:", answerStatus);
    setAnswerStatus(answerStatus);
  };

  /* 파일 업로드 */
  const fileUpload = async (todayId) => {
    console.log("업로드할 파일 목록:", files);
    // 파일 데이터 저장
    const fd = new FormData();
    files.forEach((file) => fd.append(`file`, file));

    await authAxios
      .post(`/today/${todayId}/file/upload`, fd, {
        headers: headers,
      })
      .then((resp) => {
        console.log("[file.js] fileUpload() success :D");
        console.log(resp.data);
        alert("게시물과 파일을 성공적으로 수정했습니다. :D");

        // 새롭게 등록한 글 상세로 이동
        navigate(`/today/${todayId}`);
      })
      .catch((err) => {
        console.log("[FileData.js] fileUpload() error :<");
        console.log(err);
      });
  };
  /* 썸네일 업로드 */
  const thumbnailUpload = async (todayId) => {
    // 썸네일이 없으면 종료
    if (thumbnails.length === 0) return;

    try {
      const fd = new FormData();
      fd.append("thumbnail", thumbnails[0]);

      const response = await authAxios.post(
        `/today/${todayId}/file/thumbnail/upload`,
        fd,
        { headers: headers }
      );

      console.log("[TodayUpdate.js] thumbnailUpload() success :D");
      console.log(response.data);
      alert("썸네일 업로드 성공 :D");
    } catch (error) {
      console.log("[TodayUpdate.js] thumbnailUpload() error :<");
      console.log(error);
      alert("썸네일 업로드 실패 :(");
    }
  };

  /* 파일 삭제 */
  const fileDelete = async (todayId, fileId) => {
    try {
      await authAxios.delete(`/today/${todayId}/file/delete?fileId=${fileId}`, {
        headers: headers,
      });
      console.log("[TodayUpdate.js] fileDelete() success :D");
      alert("파일 삭제 성공 :D");
    } catch (error) {
      console.error("[TodayUpdate.js] fileDelete() error :<");
      console.error(error);
    }
  };

  /* 게시판 수정 */
  const updateToday = async () => {
    const req = {
      id: auth,
      title: title,
      content: content,
      answerStatus: answerStatus,
    };

    try {
      const updateResponse = await authAxios.patch(
        `/today/${today.todayId}/update`,
        req,
        {
          headers: headers,
        }
      );

      console.log("[TodayUpdate.js] updateToday() success :D");
      console.log(updateResponse.data);
      const updatedTodayId = updateResponse.data.todayId;

      if (files.length > 0) {
        await fileUpload(updatedTodayId);
      }

      // 썸네일이 존재하는지 확인하고 업로드
      if (thumbnails.length > 0) {
        await thumbnailUpload(updatedTodayId);
      }

      alert("게시글을 성공적으로 수정했습니다 :D");
      navigate(`/today/${updatedTodayId}`);
    } catch (error) {
      console.log("[TodayUpdate.js] updateToday() error :<");
      console.log(error);
    }
  };

  return (
    <>
      <div className="create_container">
        <div className="create_flex_column">
          <div className="create_categoryname_divide">
            <div className="create_category_name">
              <div className="create_category list_style">
                <h4 className="create_h4">분류</h4>
                <span>빈즈투데이</span>
              </div>
              <div className="create_row">
                <div className="create_text_category">
                  <div>
                    <strong>글 내용</strong>
                  </div>
                  <select
                    className="form-control"
                    value={answerStatus}
                    onChange={handleChangeStatus}
                  >
                    <option value="IN_PROGRESS">궁금해요</option>
                    <option value="COMPLETED">해결됬어요!</option>
                    <option value="NOT_APPLICABLE">잡담</option>
                  </select>
                </div>
              </div>
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
        <div className="create_flex_row">
          <div className="create_left_wrap list_style">
            <h4 className="create_h4">내용</h4>
            <textarea
              name="today_content"
              value={content}
              onChange={changeContent}
              placeholder="게시글 내용을 입력하세요"
            ></textarea>
          </div>
          <div className="create_right_wrap list_style">
            <div>
              <div className="create_file">
                <h4>썸네일</h4>
                <span>파일을 등록해주세요</span>
                <div className="create_view_file">
                  {thumbnails.map((thumbnail, index) => (
                    <div key={index}>
                      <img
                        src={URL.createObjectURL(thumbnail)}
                        alt={`Thumbnail ${index}`}
                        style={{ width: "280px", height: "280px" }}
                      />
                      <button onClick={() => handleRemoveThumbnail(index)}>
                        x
                      </button>
                    </div>
                  ))}
                  {thumbnails.length === 0 && (
                    <span>
                      최대 파일 크기 : 5MB <br />
                      이미지 사이즈 : 420 * 300 (px) <br />
                      확장자 : JPG, JPEG, PNG, GIF, TIFF, BMP, EPS, SVG
                    </span>
                  )}
                </div>
                <input
                  type="file"
                  name="thumbnail"
                  onChange={handleChangeThumbnail}
                />
              </div>
              <div className="create_file file_border">
                <h4 style={{ marginTop: "20px" }}>첨부파일</h4>
                <span>파일을 불러와주세요</span>
                {severFiles.length > 0 || files.length > 0 ? (
                  <div className="file-box">
                    <ul>
                      {/* 기존의 파일 데이터, 삭제 로직 */}
                      {severFiles.map((file, index) => (
                        <li
                          key={file.fileId}
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                          }}
                        >
                          <span>
                            <strong>File Name:</strong> {file.originFileName}{" "}
                            &nbsp;
                            <button
                              className="delete-button"
                              type="button"
                              onClick={() =>
                                handleRemoveSeverFile(
                                  index,
                                  todayId,
                                  file.fileId
                                )
                              }
                            >
                              x
                            </button>
                          </span>
                        </li>
                      ))}
                      {/* 새로운 파일을 저장할 때 */}
                      {files.map((file, index) => (
                        <li
                          key={file.fileId}
                          style={{
                            display: "flex",
                            justifyContent: "space-between",
                            alignItems: "center",
                          }}
                        >
                          <span>
                            <strong>File Name:</strong> {file.name} &nbsp;
                            <button
                              className="delete-button"
                              type="button"
                              onClick={() => handleRemoveFile(index)}
                            >
                              x
                            </button>
                          </span>
                        </li>
                      ))}
                    </ul>
                  </div>
                ) : (
                  <div className="file-box">
                    <p>No files</p>
                  </div>
                )}
                <div className="file-select-box">
                  <input
                    type="file"
                    name="file"
                    onChange={handleChangeFile}
                    multiple="multiple"
                  />
                </div>
              </div>
              <button
                type="submit"
                className="create_link"
                onClick={updateToday}
              >
                수정하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
export default TodayUpdate;
