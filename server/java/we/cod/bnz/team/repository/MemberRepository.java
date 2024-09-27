package we.cod.bnz.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.team.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  List<Member> findByTeamId(Long teamId);

  @Query(value = " SELECT * FROM Member m " +
          " JOIN Account a  ON a.id = m.account_id " +
          " JOIN Teams t    ON t.id = m.team_id " +
          " WHERE m.team_id = :team_id " +
          " AND   m.account_id = :account_id ", nativeQuery = true)
  Optional<Member> findByAccountAndTeam(@Param("account_id") Long accountId,
                                        @Param("team_id") Long teamId);

  @Query(value = " SELECT M.* " +
          " FROM members M " +
          " JOIN Team T     ON T.id = M.team_id " +
          " JOIN account A  ON A.id = M.account_id " +
          " WHERE T.id  IN  ( SELECT team_id " +
          "                   FROM members " +
          "                   WHERE account_id = :id ) ", nativeQuery = true)
  List<Member> findMyMember(@Param("id") Long id);

}

