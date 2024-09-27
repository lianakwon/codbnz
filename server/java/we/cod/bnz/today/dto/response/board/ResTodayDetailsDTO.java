package we.cod.bnz.today.dto.response.board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.dto.response.comment.ResCommentTodayDTO;
import we.cod.bnz.today.dto.response.file.ResTodayDetailsFileDTO;
import we.cod.bnz.today.entity.Today;

import java.util.List;
import java.util.stream.Collectors;

/**
 * -Response-
 * 게시글 상세, 수정 요청에 대한 정보를 반환
 */

@Getter
@Setter
@NoArgsConstructor
public class ResTodayDetailsDTO {

    // board info
    private Long todayId;
    private String title;
    private String content;
    private int viewCount;
    private String writerId;
    private String writerName;
    private String createdDate;
    private String modifiedDate;
    private AnswerStatus answerStatus;
    private String thumbnailPath;
    private int commentCount;
    private int likeCount;
    private int questionCount;
    private String profilePictureFileName;
    private String profileMSG;

    // comments
    private List<ResCommentTodayDTO> comments;

    // file
    private List<ResTodayDetailsFileDTO> files;


    @Builder
    public ResTodayDetailsDTO(Long todayId,
                              String title,
                              String content,
                              int viewCount,
                              int likeCount,
                              int questionCount,
                              String writerName,
                              String writerId,
                              String createdDate,
                              String modifiedDate,
                              List<ResCommentTodayDTO> comments,
                              List<ResTodayDetailsFileDTO> files,
                              AnswerStatus answerStatus,
                              String thumbnailPath,
                              String profilePictureFileName,
                              String profileMSG) {
        this.todayId = todayId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.questionCount = questionCount;
        this.writerId = writerId;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.comments = comments;
        this.files = files;
        this.answerStatus = answerStatus;
        this.thumbnailPath = thumbnailPath;
        this.profilePictureFileName = profilePictureFileName;
        this.profileMSG = profileMSG;

    }

    public static ResTodayDetailsDTO fromEntity(Today today, String profilePictureFileName) {
        return ResTodayDetailsDTO.builder()
                .todayId(today.getId())
                .title(today.getTitle())
                .content(today.getContent())
                .viewCount(today.getViewCount())
                .likeCount(today.getLikeCount())
                .questionCount(today.getQuestionCount())
                .writerId(today.getAccount().getUsername())
                .writerName(today.getAccount().getNickname())
                .createdDate(today.getCreatedDate())
                .modifiedDate(today.getModifiedDate())
                .comments(today.getComments().stream()
                        .map(commentToday -> ResCommentTodayDTO.fromEntity(commentToday, profilePictureFileName))
                        .collect(Collectors.toList()))
                .files(today.getFiles().stream()
                        .map(ResTodayDetailsFileDTO::fromEntity)
                        .collect(Collectors.toList()))
                .answerStatus(today.getAnswerStatus())
                .thumbnailPath(today.getThumbnailPath())
                .profilePictureFileName(profilePictureFileName)
                .profileMSG(today.getAccount() != null ? today.getAccount().getProfileMSG() : null)
                .build();
    }
}
