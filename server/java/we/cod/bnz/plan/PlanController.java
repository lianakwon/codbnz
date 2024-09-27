package we.cod.bnz.plan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.plan.dto.PlanRequestDto;
import we.cod.bnz.plan.dto.PlanResponseDto;
import we.cod.bnz.plan.dto.PlanUpdateRequestDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {

    private final PlanService planService;

    // 캘린더 일정 등록
    @PostMapping
    public ResponseEntity<PlanResponseDto> addEvent(@RequestBody PlanRequestDto request, @AuthenticationPrincipal UserPrincipal account) {
        Plan plan = planService.addEvent(request, account);
        PlanResponseDto response = new PlanResponseDto(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 캘린더 전체 일정 보기
    @GetMapping
    public ResponseEntity<List<PlanResponseDto>> findAllEvent(@AuthenticationPrincipal UserPrincipal account) {
        System.out.println("account = " + account);
        List<Plan> plans = planService.findAllEvent(account);
        List<PlanResponseDto> responses = plans.stream().map(PlanResponseDto::new).collect(Collectors.toList());
        System.out.println(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    // 캘린더 날짜로 일정 조회
    @GetMapping("/date/{formattedDate}")
    public ResponseEntity<PlanResponseDto> findDateEvent(@PathVariable("formattedDate") LocalDate date,
                                                          @AuthenticationPrincipal UserPrincipal account) {
        Plan plan = planService.findDateEvent(date, account);
        PlanResponseDto response = new PlanResponseDto(plan);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<PlanResponseDto> detailEvent(@PathVariable("eventId") Long eventId,
                                                        @AuthenticationPrincipal UserPrincipal account) {
        Plan plan = planService.detailEvent(eventId, account);
        PlanResponseDto response = new PlanResponseDto(plan);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<PlanResponseDto> updateEvent(@PathVariable("eventId") Long eventId,
                                                        @RequestBody PlanUpdateRequestDto request,
                                                        @AuthenticationPrincipal UserPrincipal account) {
        Plan plan = planService.updateEvent(account, eventId, request);
        PlanResponseDto response = new PlanResponseDto(plan);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/planIdTop5Desc")
    public ResponseEntity<List<PlanResponseDto>> findByTop5PlanId(@AuthenticationPrincipal UserPrincipal account) {
        List<Plan> plans = planService.findByTop5PlanId(account);
        List<PlanResponseDto> responses = plans.stream().map(PlanResponseDto::new).collect(Collectors.toList());
        System.out.println(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);

    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable("eventId") Long eventId,
                                              @AuthenticationPrincipal UserPrincipal account) {
        planService.deleteEvent(account, eventId);
        return ResponseEntity.status(HttpStatus.OK).body("일정 삭제 완료");
    }
}
