package we.cod.bnz.today.dto.request.board;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.common.AnswerStatus;

/**
 * -Request-
 * 게시글 수정 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class TodayUpdateDTO {

    private String title;
    private String content;
    private AnswerStatus answerStatus;
    private String thumbnailPath;

    @Builder
    public TodayUpdateDTO(String title,
                          String content,
                          AnswerStatus answerStatus,
                          String thumbnailPath) {
        this.title = title;
        this.content = content;
        this.answerStatus = answerStatus;
        this.thumbnailPath = thumbnailPath;
    }
}
