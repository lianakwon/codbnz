package we.cod.bnz.today.dto.request.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.entity.Today;

/**
 * -Request-
 * 게시글 등록 정보 요청, 작성자는 Authentication 받음
 */

@Getter
@Setter
@NoArgsConstructor
public class TodayWriteDTO {

    private String title;
    private String content;
    private AnswerStatus answerStatus;
    private String thumbnailPath;


    public TodayWriteDTO(String title,
                         String content,
                         AnswerStatus answerStatus,
                         String thumbnailPath) {
        this.title = title;
        this.content = content;
        this.answerStatus = answerStatus;
        this.thumbnailPath = thumbnailPath;
    }

    @Builder
    public static Today ofEntity(TodayWriteDTO dto) {
        return Today.builder()
                .title(dto.title)
                .content(dto.content)
                .answerStatus(dto.answerStatus)
                .thumbnailPath(dto.thumbnailPath)
                .build();
    }
}
