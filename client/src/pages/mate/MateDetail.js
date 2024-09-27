import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { HttpHeadersContext } from "../../HttpHeadersProvider";
import MateCmmntList from "./MateCmmntList";
import MateCmmntWrite from "./MateCmmntWrite";

const MateDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { headers } = useContext(HttpHeadersContext);
  const [mate, setMate] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchMateDetail();
    increaseHits();
  }, [id]);

  const fetchMateDetail = async () => {
    try {
      console.log(id)
      const response = await axios.get(`http://localhost:8080/mate/detail/${id}`,
       { headers: headers, });
      console.log(response)
      setMate(response.data);
    } catch (error) {
      console.error("게시글 데이터를 불러오는데 실패했습니다.", error);
      setError("게시글 데이터를 불러오는데 실패했습니다.");
    }
  };

  const increaseHits = async () => {
    try {
      await axios.put(`http://localhost:8080/mate/hits/${id}`, null, {
        headers: headers,
      });
    } catch (error) {
      console.error("조회수 증가 중 오류 발생:", error);
    }
  };

  const handleCommentAdded = (newComment) => {
    setMate((prevMate) => ({
      ...prevMate,
      comments: [...prevMate.comments, newComment],
    }));
  };

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div>
      {mate ? (
        <div>
          <h1>{mate.title}</h1>
          <p>{mate.content}</p>
          {/* 댓글 목록 */}
          <MateCmmntList mateId={id} />
          {/* 댓글 작성 */}
          <MateCmmntWrite mateId={id} onCommentAdded={handleCommentAdded} />
        </div>
      ) : (
        <div>Loading...</div>
      )}
    </div>
  );
};

export default MateDetail;
