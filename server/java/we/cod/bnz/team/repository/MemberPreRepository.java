package we.cod.bnz.team.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.cod.bnz.team.entity.MemberPre;

public interface MemberPreRepository extends JpaRepository<MemberPre, Long> {

  MemberPre findByAccountIdAndTeamId(Long accountId, Long teamId);
}
