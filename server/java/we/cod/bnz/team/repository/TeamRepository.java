package we.cod.bnz.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.team.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

  @Query(value = "SELECT T.* " +
          " FROM teams T, members M, account A " +
          " WHERE 1 = 1 " +
          " AND M.team_id = T.id " +
          " AND A.id = M.account_id " +
          " AND A.id = :id " , nativeQuery = true)
  List<Team> find_by_from(@Param("id") Long id);

  @Query(value = "SELECT T.* " +
          " FROM teams T, members_pre M, account A " +
          " WHERE 1 = 1 " +
          " AND M.team_id = T.id " +
          " AND A.id = M.account_id " +
          " AND M.account_id = :id ", nativeQuery = true)
  List<Team> get_invites(@Param("id") Long id);
}
