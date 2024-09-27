package we.cod.bnz.today.dto.response.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.account.Account;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.entity.Today;

/**
 * -Response-
 * 게시글 등록 반환 정보
 */

@Getter
@Setter
@NoArgsConstructor
public class ResTodayWriteDTO {

    private Long todayId;
    private String title;
    private String content;
    private String writerId;
    private String writerName;
    private String createdDate;
    private AnswerStatus answerStatus;
    private String thumbnailPath;



    @Builder
    public ResTodayWriteDTO(Long todayId,
                            String title,
                            String content,
                            String writerName,
                            String createdDate,
                            AnswerStatus answerStatus,
                            String thumbnailPath,
                            String writerId) {
        this.todayId = todayId;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.answerStatus = answerStatus;
        this.thumbnailPath = thumbnailPath;
        this.writerId = writerId;
    }

    public static ResTodayWriteDTO fromEntity(Today today, Account account) {
        return ResTodayWriteDTO.builder()
                .todayId(today.getId())
                .title(today.getTitle())
                .content(today.getContent())
                .writerId(account.getUsername())
                .writerName(account.getNickname())
                .createdDate(today.getCreatedDate())
                .answerStatus(today.getAnswerStatus())
                .thumbnailPath(today.getThumbnailPath())
                .build();
    }
}
