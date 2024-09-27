package we.cod.bnz.today.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import we.cod.bnz.account.Account;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Today extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "TODAY_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(name = "ANSWER_STATUS")
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;

    @Column(name = "VIEW_COUNT")
    @Builder.Default // @Builder.Default 추가
    private int viewCount = 0;

    @Setter
    @Column(name = "COMMENT_COUNT")
    @Builder.Default // @Builder.Default 추가
    private int commentCount = 0;

    @Column(name = "LIKE_COUNT")
    @Builder.Default // @Builder.Default 추가
    private int likeCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "today_likes",
            joinColumns = @JoinColumn(name = "TODAY_ID"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @Builder.Default
    private Set<Account> likedAccounts = new HashSet<>();

    @Column(name = "QUESTION_COUNT")
    @Builder.Default // @Builder.Default 추가
    private int questionCount = 0; // 기본값 설정

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "today_questions",
            joinColumns = @JoinColumn(name = "today_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @Builder.Default
    private Set<Account> questionAccounts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "today", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @Builder.Default
    private List<CommentToday> comments = new ArrayList<>();

    @OneToMany(mappedBy = "today", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10)
    @Builder.Default
    private List<FileEntity> files = new ArrayList<>();

    @Setter
    @Column(name = "THUMBNAIL_PATH", columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String thumbnailPath;

    //== 조회수 증가 ==//
    public void upViewCount() {
        this.viewCount++;
    }

    //== 수정 Dirty Checking ==//
    public void update(String title, String content, AnswerStatus answerStatus) {
        this.title = title;
        this.content = content;
        this.answerStatus = answerStatus;
    }

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

    // 궁금해요 추가
    public void addQuestion(Account account) {
        questionAccounts.add(account);
        questionCount++;
    }

    // 궁금해요 제거
    public void removeQuestion(Account account) {
        questionAccounts.remove(account);
        questionCount--;
    }
}