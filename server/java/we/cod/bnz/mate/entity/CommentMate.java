package we.cod.bnz.mate.entity;

import jakarta.persistence.*;
import lombok.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.mate.dto.CommentMateDTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments_m")
public class CommentMate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @Column(name = "create_date", nullable = false, updatable = false)
  private LocalDateTime create_date;

  @Column(name = "update_date", nullable = false)
  private LocalDateTime update_date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mate_id", nullable = false)
  private Mate mate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id", nullable = false)
  private Account writer;

  @ElementCollection
  private Set<Account> likedAccounts = new HashSet<>();

  public void like(Account account) {
    likedAccounts.add(account);
  }

  public void unlike(Account account) {
    likedAccounts.remove(account);
  }

  public Long getLikes() {
    return Long.valueOf(likedAccounts.size());
  }

  public CommentMateDTO toDTO() {
    return new CommentMateDTO(id, content, writer.toDTO(), mate.toDTO(), create_date, update_date, getLikes());
  }

  public static CommentMate createCommentMate(Account writer, Mate mate, CommentMateDTO dto) {
    return new CommentMate(null, dto.getContent(), LocalDateTime.now(), LocalDateTime.now(), mate, writer, new HashSet<>());
  }
}
