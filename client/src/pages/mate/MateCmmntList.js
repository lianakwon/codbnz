import React, { useState, useEffect } from "react";
import axios from "axios";
import MateCmmnt from "./MateCmmnt";
import "../../assets/mateCmmnt.scss";

const MateCmmntList = ({ mateId }) => {
  const [comments, setComments] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchComments = async () => {
      console.log(mateId)
      try {
        const response = await axios.get(`/mates/comments/${mateId}`);
        console.log(response)
        setComments(response.data);
      } catch (error) {
        console.error("댓글을 가져오는 중 오류 발생:", error);
        setError(error);
      }
    };
    fetchComments();
  }, [mateId]);

  const handleLike = async (commentId) => {
    try {
      const response = (await axios.post(`/mates/comments/${commentId}/like`));
      console.log(response)
      setComments((prevComments) =>
        prevComments.map((comment) =>
          comment.id === commentId
            ? { ...comment, likes: comment.likes + 1 }
            : comment
        )
      );
    } catch (error) {
      console.error("좋아요 처리 중 오류 발생:", error);
      setError(error);
    }
  };

  const handleUnlike = async (commentId) => {
    try {
      const response = await axios.post(`/mates/comments/${commentId}/unlike`);
      console.log(response)
      setComments((prevComments) =>
        prevComments.map((comment) =>
          comment.id === commentId
            ? { ...comment, likes: comment.likes - 1 }
            : comment
        )
      );
    } catch (error) {
      console.error("좋아요 취소 처리 중 오류 발생:", error);
      setError(error);
    }
  };

  const handleDelete = async (commentId) => {
    try {
      const response = await axios.delete(`/mates/comments/${commentId}`);
      console.log(response)
      setComments((prevComments) =>
        prevComments.filter((comment) => comment.id !== commentId)
      );
    } catch (error) {
      console.error("댓글 삭제 중 오류 발생:", error);
      setError(error);
    }
  };

  const handleEdit = (commentId) => {
    // 편집 기능 구현
  };

  if (!Array.isArray(comments)) {
    return <div>댓글을 불러오는 중 오류가 발생했습니다.</div>;
  }

  return (
    <div className="comments-container">
      {comments.map((comment) => (
        <MateCmmnt
          key={comment.id}
          comment={comment}
          onLike={handleLike}
          onUnlike={handleUnlike}
          onDelete={handleDelete}
          onEdit={handleEdit}
        />
      ))}
    </div>
  );
};

export default MateCmmntList;
