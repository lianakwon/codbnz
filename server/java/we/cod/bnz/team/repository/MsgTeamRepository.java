package we.cod.bnz.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.team.entity.MsgTeam;

import java.util.List;

public interface MsgTeamRepository extends JpaRepository<MsgTeam, Long> {

  @Query(value = " SELECT M.*  FROM msg_team M " +
          " JOIN teams T      ON T.id = M.team_id " +
          " WHERE T.id        IN ( SELECT T2.id FROM teams T2 " +
            "                     JOIN members M2   ON M2.team_id = T2.id " +
          "                       JOIN account A2   ON M2.account_id = A2.id " +
          "                       WHERE A2.id = :id ) ", nativeQuery = true)
  List<MsgTeam> findByFrom(@Param("id") Long roomId);
}
