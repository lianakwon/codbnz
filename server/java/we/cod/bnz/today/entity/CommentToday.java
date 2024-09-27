package we.cod.bnz.today.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.Account;
import we.cod.bnz.today.common.BaseTimeEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentToday extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TODAY_ID")
    private Today today;

    @Column(name = "LIKE_COUNT")
    @Builder.Default // @Builder.Default 추가
    private int likeCount = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "COMMENT_ID"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @Builder.Default
    private Set<Account> likedAccounts = new HashSet<>();

    // 좋아요 추가
    public void addLike(Account account) {
        likedAccounts.add(account);
        likeCount++;
    }

    // 좋아요 제거
    public void removeLike(Account account) {
        likedAccounts.remove(account);
        likeCount--;
    }

    // Board와의 다대일(N:1) 관계를 설정하는 메소드
    public void setToday(Today today) {
        this.today = today;
        today.getComments().add(this); // Board 엔티티에도 Comment를 추가합니다.
    }

    // update
    public void update(String content) {
        this.content = content;
    }
}