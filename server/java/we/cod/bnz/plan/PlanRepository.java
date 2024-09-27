package we.cod.bnz.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import we.cod.bnz.account.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {


    // 특정 사용자 전체 일정
    List<Plan> findByAccount(Account account);

    // 특정 사용자 일정 조회
    Optional<Plan> findByAccountIdAndId(Long accountId, Long planId);

    void deleteByAccountIdAndId(Long accountId, Long planId);

    Optional<Plan> findByAccountIdAndDate(Long accountId, LocalDate date);


    List<Plan> findTop5ByAccountIdOrderByIdDesc(Long accountId); // 특정 accountId에 해당하는 Plan을 id 역순으로 정렬하여 상위 5개를 조회
}
