package we.cod.bnz.today.dto.response.comment;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.entity.CommentToday;

/**
 * -Response-
 * 댓글 등록, 수정 응답
 */

@Getter
@Setter
@NoArgsConstructor
public class ResCommentTodayDTO {

    private Long commentId;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private String commentWriterId;
    private String commentWriterName; // 댓글 작성자
    private String profilePictureFileName; //댓글작성자 프로필사진
    private int likeCount;

    @Builder
    public ResCommentTodayDTO(Long commentId, String content, String createdDate, String modifiedDate, String commentWriterName, String profilePictureFileName, int likeCount,String commentWriterId) {
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.commentWriterName = commentWriterName;
        this.profilePictureFileName = profilePictureFileName;
        this.likeCount = likeCount;
        this.commentWriterId = commentWriterId;

    }

    public static ResCommentTodayDTO fromEntity(CommentToday commentToday, String profilePictureFileName) {
        return ResCommentTodayDTO.builder()
                .commentId(commentToday.getId())
                .content(commentToday.getContent())
                .createdDate(commentToday.getCreatedDate())
                .modifiedDate(commentToday.getModifiedDate())
                .commentWriterId(commentToday.getAccount().getUsername())
                .commentWriterName(commentToday.getAccount().getNickname())
                .profilePictureFileName(profilePictureFileName)
                .likeCount(commentToday.getLikeCount())
                .build();
    }
}
