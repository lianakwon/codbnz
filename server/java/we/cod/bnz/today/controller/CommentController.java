package we.cod.bnz.today.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.AccountService;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.today.dto.request.comment.CommentTodayDTO;
import we.cod.bnz.today.dto.response.LikeResponseDTO;
import we.cod.bnz.today.dto.response.comment.ResCommentTodayDTO;
import we.cod.bnz.today.service.CommentService;
import we.cod.bnz.today.service.TodayService;

import java.util.Optional;

@RestController
@RequestMapping("/today/{todayId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TodayService todayService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @GetMapping("/list")
    public ResponseEntity<Page<ResCommentTodayDTO>> commentList(
            @PathVariable Long todayId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ResCommentTodayDTO> commentList = commentService.getAllComments(pageable, todayId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    @PostMapping("/write")
    public ResponseEntity<ResCommentTodayDTO> write(
            @AuthenticationPrincipal UserPrincipal account,
            @PathVariable Long todayId,
            @RequestBody CommentTodayDTO commentTodayDto) {

        ResCommentTodayDTO saveCommentDTO = commentService.write(todayId, account, commentTodayDto);
        todayService.updateCommentCount(todayId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity<ResCommentTodayDTO> update(
            @PathVariable Long commentId,
            @RequestBody CommentTodayDTO commentTodayDto) {

        ResCommentTodayDTO updateCommentDTO = commentService.update(commentId, commentTodayDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Long> delete(
            @PathVariable Long commentId,
            @PathVariable Long todayId,
            @AuthenticationPrincipal UserPrincipal account){
        commentService.delete(commentId, account);
        todayService.updateCommentCount(todayId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<String> addLikeToComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal account) {
        String message = commentService.addLikeToComment(commentId, account);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/profileimg")
    public ResponseEntity<String> getProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // 현재 접속한 사용자의 정보를 가져옵니다.
            Optional<Account> account = accountRepository.findByUsername(userPrincipal.getUsername());

            // 사용자 계정 정보를 이용하여 프로필 사진 파일명을 가져옵니다.
            String profilePictureFileName = commentService.getUserProfilePicture(account.orElse(null));

            // 프로필 사진 파일명을 클라이언트에 반환합니다.
            return ResponseEntity.ok(profilePictureFileName);
        } catch (Exception e) {
            // 에러 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
