package we.cod.bnz.account.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.mate.MateService;
import we.cod.bnz.mate.dto.*;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.mate.entity.Tag;
import we.cod.bnz.mate.repository.MateRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static we.cod.bnz.mate.entity.QMate.mate;
import static we.cod.bnz.mate.entity.QTag.tag;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/mate")
public class MateController {

  private final MateService service;
  private final AccountRepository accountRepo;
  private final MateRepository mateRepo;

  // 검색
  @GetMapping("/mates/search")
  public ResponseEntity<List<ResponseMate>> searchMates(@RequestParam String keyword) {
    List<Mate> mates = service.searchMates(keyword);
    List<ResponseMate> responseMates = mates.stream()
            .map(ResponseMate::new)
            .collect(Collectors.toList());
    return ResponseEntity.ok(responseMates);
  }

  // 조회수
  @PutMapping("/increaseHits/{mateId}")
  public ResponseEntity<MateDTO> increaseHits(@PathVariable("mateId") Long mateId) {
    MateDTO mateDTO = service.increaseHits(mateId);
    return ResponseEntity.ok(mateDTO);
  }
  // 게시글 작성
  @PostMapping("/create")
  public ResponseEntity<ResponseMate> createMate(@RequestBody MateListDTO mateListDTO, @AuthenticationPrincipal UserPrincipal user) {
    try {
      ResponseMate responseMate = service.createMate(mateListDTO, user);
      return ResponseEntity.ok(responseMate);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }




  // 게시글 상세 조회
  @GetMapping("/detail/{id}")
  public ResponseEntity<ResponseMate> getMateDetail(@PathVariable Long id) {
    try {
      ResponseMate responseMate = service.getMateDetail(id);
      return ResponseEntity.ok(responseMate);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // 게시글 전체 조회
  @GetMapping({"/", ""})
  public ResponseEntity<List<ResponseMate>> getAllMates() {
    List<ResponseMate> mates = service.getAllMates();
    return ResponseEntity.ok(mates);
  }

  // 게시글 수정
  @PutMapping("/update/{id}")
  public ResponseEntity<ResponseMate> updateMate(@PathVariable Long id, @RequestBody MateDTO mateDTO) {
    try {
      ResponseMate responseMate = service.updateMate(id, mateDTO);
      return ResponseEntity.ok(responseMate);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  // 게시글 삭제
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Void> deleteMate(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    try {
      boolean result = service.deleteMate(id, userDetails.getUsername());
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  // 게시글 좋아요
  @PostMapping("/like/{id}")
  public ResponseEntity<Void> likeMate(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
    try {
      service.likeMate(id, userDetails.getUsername());
      return ResponseEntity.ok().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // 댓글 조회
  // get_all_comments_mate
  @GetMapping("/get_all_comments_mate/{mate_id}")
  public ResponseEntity<List<ResponseComment>> getAllCommentsMate(@PathVariable("mate_id") Long mateId) {
    try {
      List<ResponseComment> comments = service.getAllCommentsMate(mateId);
      return ResponseEntity.ok(comments);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.notFound().build();
    }
  }


  @GetMapping("/comments/{mateId}")
  public ResponseEntity<List<ResponseComment>> getComments(@PathVariable("mateId") Long mateId) {
    List<ResponseComment> comments = service.getAllCommentsMate(mateId);
    return ResponseEntity.ok(comments);
  }

  // 댓글 작성
  // create_comments_mate("/{account_id}/{mate_id}")
  @PostMapping("/comment/create")
  public ResponseEntity<CommentMateDTO> createComment(@RequestParam Long mateId,
                                                      @RequestParam String content,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      CommentMateDTO comment = service.createCommentMate(mateId, content, userDetails.getUsername());
      return ResponseEntity.ok(comment);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  // 댓글 수정
// update_comments_mate("/{account_id}/{commentId}")
  @PutMapping("/comments/{commentId}")
  public ResponseEntity<ResponseComment> updateCommentMate(@PathVariable Long commentId, @RequestBody String content, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    ResponseComment comment = service.updateCommentMate(commentId, content, username);
    return ResponseEntity.ok(comment);
  }

  // 댓글 삭제
// delete_comments_mate ("/{account_id}/{commentId}")
  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Void> deleteCommentMate(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    service.deleteCommentMate(commentId, username);
    return ResponseEntity.noContent().build();
  }

  // 댓글 좋아요
  @PostMapping("/comments/{commentId}/like")
  public ResponseEntity<ResponseComment> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    ResponseComment comment = service.likeComment(commentId, username);
    return ResponseEntity.ok(comment);
  }

  @PostMapping("/comments/{commentId}/unlike")
  public ResponseEntity<ResponseComment> unlikeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    ResponseComment comment = service.unlikeComment(commentId, username);
    return ResponseEntity.ok(comment);
  }
}
