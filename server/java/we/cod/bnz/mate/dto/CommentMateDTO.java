package we.cod.bnz.mate.dto;

import lombok.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Mate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentMateDTO {

  private Long id; // 댓글 아이디

  private String content; // 내용

  private AccountDTO writer; // 작성자

  private MateDTO mate; // 게시글

  private LocalDateTime create_date; // 작성일시

  private LocalDateTime update_date; // 수정일시

  private Long likes; // 좋아요 수

  public CommentMateDTO(CommentMate comment) {
    this.id = comment.getId();
    this.content = comment.getContent();
    this.writer = comment.getWriter().toDTO();
    this.mate = comment.getMate().toDTO();
    this.create_date = comment.getCreate_date();
    this.update_date = comment.getUpdate_date();
    this.likes = comment.getLikes();
  }
}
