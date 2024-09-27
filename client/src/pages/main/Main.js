import Banner from './Banner';
import Text from "./Text";
import Icons from "./Icons";
import Mate from "./Mate";
import Today from "./Today";
import { useEffect, useState } from 'react';
import axios from 'axios';



const Main = () => {

  useEffect(() => { getMates(); getToday(); }, []);

  const [mates, setMates] = useState([]);
  const [todays, setTodays] = useState([]);

  // 메이트 최신글
  async function getMates() {
    try {
      const res = (await axios.get(`http://localhost:8080/my/boards_m`));
      setMates(res.data.map(mate => ({
        id: mate.id,
        title: mate.title,
        content: mate.content,
        author: mate.author.nickname,
        create_date: mate.create_date,
        update_date: mate.update_date,
        views: mate.hits,
        likes: mate.likes,
        tags: mate.tag
      })));
    } catch (err) { console.log(err); }
  };

  // 투데이 최신글
  async function getToday() {
    try {
      const res = (await axios.get(`http://localhost:8080/my/boards_t`));
      setTodays(res.data.map(today => ({
        id: today.todayId,
        title: today.title,
        content: today.content,
        thum: today.thumbnailPath,
        author: today.writerName,
        answered: today.answerStatus,
        views: today.viewCount,
        likes: today.likeCount,
        quest: today.questionCount,
        comments: today.commentCount,
      })));
    } catch (err) { console.log(err); }
  };




  return (
    <>
      <div className="inner_m">
        <Banner />
        <Text />
        <Icons />
        <Mate props={mates} />
        <Today props={todays} />
      </div>
    </>
  )
}

export default Main