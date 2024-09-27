import React, { useContext, useState } from "react";
import { AuthContext } from "../../AuthProvider";

const MateCmmntWrite = ({ mateId }) => {
  const { auth } = useContext(AuthContext);
  const [comment, setComment] = useState("");

  if (!auth) {
    return <div>Loading...</div>;
  }

  if (!auth.isAuthenticated) {
    return <div>You need to be logged in to write a comment.</div>;
  }

  const handleCommentChange = (e) => {
    setComment(e.target.value);
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    // 댓글 작성 로직
  };

  return (
    <div>
      <form onSubmit={handleCommentSubmit}>
        <textarea value={comment} onChange={handleCommentChange}></textarea>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
};

export default MateCmmntWrite;
