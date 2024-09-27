package we.cod.bnz.plan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.plan.dto.PlanRequestDto;
import we.cod.bnz.plan.dto.PlanUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {

    private final PlanRepository planRepository;
    private final AccountRepository accountRepository;


    // 캘린더 일정 등록
    @Transactional
    public Plan addEvent(PlanRequestDto request, UserPrincipal username) {
        Plan plan = PlanRequestDto.ofEntity(request);
        Account planAccount = getAccountUsername(username);
        plan.setMappingMember(planAccount);
        return planRepository.save(plan);
    }

    // 캘린더 상세 일정 보기
    public Plan detailEvent(Long planId, UserPrincipal username) {
        // 로그인한 사용자를 식별
        Account planAccount = getAccountUsername(username);

        // 해당 이벤트를 찾고, 해당 사용자가 추가한 것인지 확인
        Optional<Plan> findEvent = planRepository.findByAccountIdAndId(planAccount.getId(), planId);
        return findEvent.orElseThrow(() -> new IllegalArgumentException("Plan ID를 찾을 수 없음" + planId ));
    }

    // 캘린더 전체 일정 보기
    public List<Plan> findAllEvent(UserPrincipal username) {
        Account planAccount = getAccountUsername(username);
        System.out.println(planAccount.getId());
        List<Plan> plans = planRepository.findByAccount(planAccount);
        return plans;
    }

    // 캘린더 일정 수정
    @Transactional
    public Plan updateEvent(UserPrincipal username ,Long planId, PlanUpdateRequestDto request) {
        Account planAccount = getAccountUsername(username);
        Plan findPlan = planRepository.findByAccountIdAndId(planAccount.getId(), planId)
                .orElseThrow(()-> new IllegalArgumentException("Plan ID를 찾을 수 없음" + planId ));
        findPlan.updateEvent(request);
        planRepository.save(findPlan);
        return findPlan;
    }

    // 캘린더 일정 삭제
    @Transactional
    public void deleteEvent(UserPrincipal username, Long eventId) {
        Account planAccount = getAccountUsername(username);
        planRepository.deleteByAccountIdAndId(planAccount.getId(), eventId);
    }

    public Plan findDateEvent(LocalDate date, UserPrincipal username) {
        Account planAccount = getAccountUsername(username);
        Plan findDateEvent = planRepository.findByAccountIdAndDate(planAccount.getId(), date)
                .orElseThrow(()-> new IllegalArgumentException("Plan 날짜를 찾을 수 없음" + date ));
        return findDateEvent;
    }

    // id 역순으로 정렬하여 상위 5개를 조회
    public List<Plan> findByTop5PlanId(UserPrincipal username) {
        Account planAccount = getAccountUsername(username);
        List<Plan> plans = planRepository.findTop5ByAccountIdOrderByIdDesc(planAccount.getId());
        return plans;
    }


    // 로그인된 사용자를 가져오는 메서드
    private Account getAccountUsername(UserPrincipal username) {
        System.out.println("username = " + username);
        String account = username.getUsername();
        return accountRepository.findById(Long.parseLong(account))
                .orElseThrow(() -> new IllegalArgumentException("Account username" + username));
    }

}
