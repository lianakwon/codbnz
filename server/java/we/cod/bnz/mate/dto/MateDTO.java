package we.cod.bnz.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.entity.Mate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MateDTO {

  private Long id; // 제목

  private String title; // 제목

  private String content; // 내용

  private AccountDTO author; // 작성자

  private LocalDateTime create_date; // 작성일시

  private LocalDateTime update_date; // 수정일시

  private Long hits; // 조회수

  private MateType type; // 태그 | 스터디 or 프로젝트

  private Long commentCnt; // 댓글수

  private Long likes; // 좋아요수

  private TagDTO tag; // 태그 리스트

  public MateDTO(Mate mate) {
    this.id = mate.getId();
    this.title = mate.getTitle();
    this.content = mate.getContent();
    this.author = mate.getAuthor().toDTO();
    this.create_date = mate.getCreate_date();
    this.update_date = mate.getUpdate_date();
    this.hits = mate.getHits();
    this.commentCnt = mate.getCommentCnt();
    this.likes = mate.getLikes();
    this.tag = mate.getTag() != null ? mate.getTag().toDTO() : null;
  }
}