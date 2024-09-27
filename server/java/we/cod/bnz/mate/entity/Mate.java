package we.cod.bnz.mate.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.account.Account;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.dto.MateDTO;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "mates")
public class Mate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private Account author;

  @Column(name = "create_date")
  private LocalDateTime create_date;

  @Column(name = "update_date")
  private LocalDateTime update_date;

  @Column(name = "hits")
  private Long hits = 0L;

  @OneToOne(mappedBy = "mate", cascade = CascadeType.ALL)
  private Tag tag;


  @Enumerated(EnumType.STRING)
  private MateType type;

  @Enumerated(EnumType.STRING)
  private MateCategory category;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "mate_likes",
          joinColumns = @JoinColumn(name = "mate_id"),
          inverseJoinColumns = @JoinColumn(name = "account_id"))
  private Set<Account> likedAccounts = new HashSet<>(); // 좋아요를 누른 유저

  @OneToMany(mappedBy = "mate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CommentMate> comments = new ArrayList<>(); // 댓글 리스트

  @Column(name = "comment_count")
  private int commentCount; // 댓글 수 추가

  // 생성자 추가
  public Mate(String title, String content, Account author, LocalDateTime create_date, LocalDateTime update_date, Long hits, MateType type, MateCategory category) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.create_date = create_date;
    this.update_date = update_date;
    this.hits = hits;
    this.type = type;
    this.category = category;
  }

  // 빌더 패턴 사용
  @Builder
  public Mate(String title, String content, Account author, LocalDateTime create_date, LocalDateTime update_date, Long hits, Tag tag) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.create_date = create_date;
    this.update_date = update_date;
    this.hits = hits;
    this.tag = tag;
  }
  // 좋아요 추가 메서드
  public void like(Account account) {
    likedAccounts.add(account);
  }

  // 좋아요 취소 메서드
  public void unlike(Account account) {
    likedAccounts.remove(account);
  }

  // 좋아요 수를 반환하는 메서드
  public Long getLikes() {
    return Long.valueOf(likedAccounts.size());
  }

  // 댓글 수를 반환하는 메서드
  public Long getCommentCnt() {
    return Long.valueOf(comments.size());
  }

  // MateDTO를 사용하여 Mate 엔티티 업데이트
  public void updateMate(MateDTO mateDTO) {
    this.title = mateDTO.getTitle();
    this.content = mateDTO.getContent();
    this.update_date = LocalDateTime.now();
    if (mateDTO.getTag() != null) {
      this.tag.updateTag(mateDTO.getTag());
    }
  }

  // toDTO 메서드 추가
  public MateDTO toDTO() {
    return new MateDTO(this);
  }
}