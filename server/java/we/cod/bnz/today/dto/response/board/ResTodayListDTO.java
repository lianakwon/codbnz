package we.cod.bnz.today.dto.response.board;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.entity.Today;


/**
 * -Response-
 * list 요청에 대한 정보를 반환, 양방향 관계로 인해 JSON 직렬화가 반복되는 문제를 해결하기 위한 DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class ResTodayListDTO {
    // 작성일, 수정일, 작성자, 댓글 개수만 전체 목록에 대한 데이터로 받으면 됨
    // 상세한 댓글 내용 등은 상세보기에서 처리
    private Long todayId;
    private String title;
    private String content;
    private int viewCount;
    private String createdDate;
    private String modifiedDate;
    private String writerId;
    private String writerName;
    private AnswerStatus answerStatus;
    private String thumbnailPath;
    private int commentCount;
    private int likeCount;
    private int questionCount;

    @Builder
    public ResTodayListDTO(Long todayId,
                           String title,
                           String content,
                           int viewCount,
                           String createdDate,
                           String modifiedDate,
                           String writerId,
                           String writerName,
                           AnswerStatus answerStatus,
                           String thumbnailPath,
                           int commentCount,
                           int likeCount,
                           int questionCount) {
        this.todayId = todayId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.writerId = writerId;
        this.writerName = writerName;
        this.answerStatus = answerStatus;
        this.thumbnailPath = thumbnailPath;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.questionCount = questionCount;
    }

    // Entity -> DTO
    public static ResTodayListDTO fromEntity(Today today) {
        return ResTodayListDTO.builder()
                .todayId(today.getId())
                .title(today.getTitle())
                .content(today.getContent())
                .viewCount(today.getViewCount())
                .createdDate(today.getCreatedDate())
                .modifiedDate(today.getModifiedDate())
                .writerId(today.getAccount().getUsername())
                .writerName(today.getAccount().getNickname())
                .answerStatus(today.getAnswerStatus())
                .thumbnailPath(today.getThumbnailPath())
                .commentCount(today.getCommentCount())
                .likeCount(today.getLikeCount())
                .questionCount(today.getQuestionCount())
                .build();
    }
}
