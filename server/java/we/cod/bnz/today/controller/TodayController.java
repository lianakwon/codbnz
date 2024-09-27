package we.cod.bnz.today.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.today.common.exception.UnauthorizedException;
import we.cod.bnz.today.dto.request.board.SearchData;
import we.cod.bnz.today.dto.request.board.TodayUpdateDTO;
import we.cod.bnz.today.dto.request.board.TodayWriteDTO;
import we.cod.bnz.today.dto.response.board.ResTodayDetailsDTO;
import we.cod.bnz.today.dto.response.board.ResTodayListDTO;
import we.cod.bnz.today.dto.response.board.ResTodayWriteDTO;
import we.cod.bnz.today.service.TodayService;

import java.util.Map;

@RestController
@RequestMapping("/today")
@RequiredArgsConstructor
@Slf4j
public class TodayController {

    private final TodayService todayService;


    // 페이징 목록
    @GetMapping("/list")
    public ResponseEntity<Page<ResTodayListDTO>> todayList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResTodayListDTO> listDTO = todayService.getAllToday(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }

    // 페이징 검색 , Get 요청 @RequestBody 사용할 수 없음
    @GetMapping("/search")
    public ResponseEntity<Page<ResTodayListDTO>> search(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String writerName) {
        SearchData searchData = SearchData.createdSearchData(title, content, writerName);
        Page<ResTodayListDTO> searchToday = todayService.search(searchData, pageable);
        return  ResponseEntity.status(HttpStatus.OK).body(searchToday);
    }

    @PostMapping("/write")
    public ResponseEntity<ResTodayWriteDTO> write(
            @RequestBody TodayWriteDTO todayWriteDto,
            @AuthenticationPrincipal UserPrincipal account) {
        ResTodayWriteDTO saveTodayDTO = todayService.write(todayWriteDto, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveTodayDTO);
    }
    //상세보기
    @GetMapping("/{todayId}")
    public ResponseEntity<ResTodayDetailsDTO> detail(@PathVariable("todayId") Long todayId) {
        ResTodayDetailsDTO resTodayDetailsDto = todayService.detail(todayId);
        return ResponseEntity.status(HttpStatus.OK).body(resTodayDetailsDto);
    }

    // 상세보기 -> 수정
    @PatchMapping("/{todayId}/update")
    public ResponseEntity<ResTodayDetailsDTO> update(
            @PathVariable Long todayId,
            @RequestBody TodayUpdateDTO todayUpdateDto,
            @AuthenticationPrincipal UserPrincipal account){
        // 사용자 ID 가져오기
        Long userId = Long.valueOf(account.getUsername());
        // 업데이트 메서드 호출
        ResTodayDetailsDTO resTodayDetailsDto = todayService.update(todayId, todayUpdateDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(resTodayDetailsDto);
    }

    // 상세보기 -> 삭제
    @DeleteMapping("/{todayId}/delete")
    public ResponseEntity<Long> delete(@PathVariable Long todayId,
                                       @AuthenticationPrincipal UserPrincipal account) {
        // 사용자 ID 가져오기
        Long userId = Long.valueOf(account.getUsername());
        try {
            todayService.delete(todayId, userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    // 좋아요 추가
    @PostMapping("/{todayId}/like")
    public ResponseEntity<String> addLikeToToday(
            @PathVariable Long todayId,
            @RequestBody Map<String, String> payload) {

        String memberId = payload.get("id");
        if (memberId == null || memberId.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing memberId");
        }

        String message = todayService.addLikeToToday(todayId, memberId);
        return ResponseEntity.ok(message);
    }
    // 궁금해요 추가
    @PostMapping("/{todayId}/question")
    public ResponseEntity<String> addQuestionToToday(
            @PathVariable Long todayId,
            @RequestBody Map<String, String> payload) {

        String memberId = payload.get("id");
        if (memberId == null || memberId.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing memberId");
        }
        String message = todayService.addQuestionToToday(todayId, memberId);
        return ResponseEntity.ok(message);
    }
}