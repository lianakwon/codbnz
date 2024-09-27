package we.cod.bnz.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.talk.entity.MsgTalk;

import java.util.List;

public interface MsgTalkRepository extends JpaRepository<MsgTalk, Long> {

  @Query(value = "SELECT M.* "        + " FROM msg_talk M " +
          " JOIN talks T "            + " ON T.id = M.talk_id " +
          " WHERE talk_id = :talk_id" , nativeQuery = true)
  List<MsgTalk> findByTalkId(@Param("talk_id") Long talk_id);

  @Query(value = "SELECT M.* " + " FROM msg_talk M " +
          " JOIN talks T1     " + " ON T1.id = M.talk_id " +
          " WHERE T1.id IN ( SELECT T2.id FROM talks T2 " +
          "                 JOIN account A1 " + " ON A1.id = T2.from_id " +
          "                 JOIN account A2 " + " ON A2.id = T2.to_id " +
          "                 WHERE T2.from_id = :id OR T2.to_id = :id ) " , nativeQuery = true)
  List<MsgTalk> findByFromId(@Param("id") Long id);
}
