import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../AuthProvider";
import { HttpHeadersContext } from "../../HttpHeadersProvider";
import { componentDidMount } from "../../Axios";

const MateCmmnt = ({ comment, onLike, onUnlike, onDelete, onEdit }) => {

  const { auth, setAuth } = useContext(AuthContext);
  const { headers, setHeaders} = useContext(HttpHeadersContext);

  useEffect(() => { get(auth); console.log(comment); console.log(auth); }, [])
  const [profile, setProfile] = useState([]);

  async function get(auth) {
    if (auth === undefined) { alert('로그인 해주세요.'); navigate('/account/login'); }
    const res = (await axios.get(`http://localhost:8080/my/get_account/${auth}`)).data;
    console.log(res);
    setProfile({ username: res.username, email: res.email, nickname: res.nickname, profileMSG: res.profileMSG });
  };



  return (<><div>
    <p className="comment-author">
      {/* {comment.writer ? comment.writer : "Unknown"} */}
    </p>
    {/* <p className="comment-content">{comment.content}</p> */}
    <p className="comment-date">
      {/* {new Date(comment.create_date).toLocaleString()} */}
    </p>
    {/* <p className="comment-likes">Likes: {comment.likes}</p> */}
    {/* {auth.username === comment.writer && (
      <div className="comment-actions">
        <button onClick={() => onEdit(comment.id)}>수정</button>
        <button onClick={() => onDelete(comment.id)}>삭제</button>
      </div>
    )} */}
    {/* <button onClick={() => onLike(comment.id)}>좋아요</button>
    <button onClick={() => onUnlike(comment.id)}>좋아요 취소</button> */}
  </div>
  </>
  );
};

export default MateCmmnt;
