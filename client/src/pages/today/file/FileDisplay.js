import React from "react";
import authAxios from "../../../interceptors";

const FileDisplay = (props) => {
  const todayId = props.todayId;
  const files = props.files;

  if (!files || files.length === 0) {
    return (
      <div className="file-box">
        <p>No files</p>
      </div>
    );
  }

  const handleClickDownload = (fileId, originFileName) => {
    authAxios
      .get(`/today/${todayId}/file/download?fileId=${fileId}`, {
        responseType: "blob",
      })
      .then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", originFileName); // 파일 이름 설정
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
      .catch((error) => {
        console.error("Error downloading file:", error);
      });
  };

  return (
    <div className="file-box">
      <ul>
        {files.map((file) => (
          <li
            key={file.fileId}
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <span>
              {/* 파일 다운로드 버튼 */}
              <a
                href="#"
                onClick={(e) => {
                  e.preventDefault();
                  handleClickDownload(file.fileId, file.originFileName);
                }}
                download
              >
                Download
              </a>
              &nbsp;
              <strong>File Name:</strong> &nbsp;
              {file.originFileName}
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FileDisplay;
