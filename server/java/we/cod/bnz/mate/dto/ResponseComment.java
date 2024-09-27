package we.cod.bnz.mate.dto;

import lombok.*;
import we.cod.bnz.mate.entity.CommentMate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseComment {
    private Long id;
    private String content;
    private String writer;
    private String profileImage; // 프로필 이미지 경로
    private String nickname; // 작성자 닉네임
    private LocalDateTime create_date;
    private LocalDateTime update_date;
    private Long likes;

    public ResponseComment(CommentMate comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getWriter().getUsername();
        // Assuming profileImage is a field in Account
        this.profileImage = comment.getWriter().getProfileMSG();
        this.nickname = comment.getWriter().getNickname();
        this.create_date = comment.getCreate_date();
        this.update_date = comment.getUpdate_date();
        this.likes = comment.getLikes();
    }
}
