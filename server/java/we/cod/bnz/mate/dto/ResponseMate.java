package we.cod.bnz.mate.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.account.Account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class ResponseMate {
    private Long id;
    private String title;
    private String author;
    private String content;
    private Long likes;
    private Set<Long> likedAccountIds; // 좋아요를 누른 사람들의 ID
    private Long commentCnt;
    private Long hits;
    private String create_date;
    private String lastModified_date;
    private String category;
    private String type;
    private TagDTO tag;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ResponseMate(Mate mate) {
        this.id = mate.getId();
        this.title = mate.getTitle();
        this.author = mate.getAuthor().getUsername();
        this.content = mate.getContent();
        this.likes = mate.getLikes();
        this.likedAccountIds = mate.getLikedAccounts().stream().map(Account::getId).collect(Collectors.toSet());
        this.commentCnt = mate.getCommentCnt();
        this.hits = mate.getHits();
        this.create_date = formatter.format(mate.getCreate_date());
        this.lastModified_date = formatter.format(mate.getUpdate_date());
        this.tag = mate.getTag() != null ? new TagDTO(mate.getTag()) : null;
    }

    public ResponseMate(Long id, String title, String content, AccountDTO author, LocalDateTime create_date, LocalDateTime update_date, Long hits, String type, String category, TagDTO tag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author.getUsername();
        this.create_date = formatter.format(create_date);
        this.lastModified_date = formatter.format(update_date);
        this.hits = hits;
        this.type = type;
        this.category = category;
        this.tag = tag;
    }
}
