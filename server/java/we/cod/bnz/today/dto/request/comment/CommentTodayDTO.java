package we.cod.bnz.today.dto.request.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * -Request-
 * 댓글 등록, 수정 요청, <br>
 * -Account, Board는 URI Resource로 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class CommentTodayDTO {

    private String content;

    @Builder
    public CommentTodayDTO(String content) {
        this.content = content;
    }

    public static CommentTodayDTO ofEntity(CommentTodayDTO dto) {
        return CommentTodayDTO.builder()
                .content(dto.content)
                .build();
    }
}
