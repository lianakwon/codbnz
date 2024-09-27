import React, { useRef } from "react";
import authAxios from "../../../interceptors";
import { useState, useEffect } from "react";
import heart from "../../../assets/icons/heart_selected_r.png";
import Comment from "./Comment.js";

function CommentList(props) {
  const todayId = props.todayId;

  // Paging
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(5);
  const [totalCnt, setTotalCnt] = useState(0);
  const [commentList, setCommentList] = useState([]);

  // comment에서 참조
  const getCommentListRef = useRef(null);

  const changePage = (page) => {
    setPage(page);
    getCommentList(page);
    getCommentListRef.current(page);
  };

  const getCommentList = async (page) => {
    await authAxios
      .get(`/today/${todayId}/comment/list`, {
        params: { page: page - 1 },
      })
      .then((resp) => {
        console.log("[TodayComment.js] getCommentList() success :D");
        console.log(resp.data);

        setPageSize(resp.data.pageSize);
        setTotalPages(resp.data.totalPages);
        setTotalCnt(resp.data.totalElements);
        setCommentList(resp.data.content);
      })
      .catch((err) => {
        console.log("[TodayComment.js] getCommentList() error :<");
        console.log(err);
      });
  };

  useEffect(() => {
    getCommentListRef.current = getCommentList;
    getCommentList(1);
  }, [todayId]);

  return (
    <>
      <div className="my-1 d-flex justify-content-center"></div>
      {commentList && commentList.length > 0 ? (
        commentList.map((comment, idx) => (
          <div className="my-5" key={idx}>
            <Comment
              obj={comment}
              key={idx}
              page={page}
              getCommentList={getCommentListRef.current}
            />
          </div>
        ))
      ) : (
        <div>댓글이 없습니다.</div>
      )}
    </>
  );
}

export default CommentList;
