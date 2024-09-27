package we.cod.bnz.today.dto.response.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.entity.Thumbnail;


/**
 * 썸네일 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ResThumbnailDTO {

    private Long thumbnailId;
    private String thumbnailFileName;
    private String thumbnailFilePath;
    private String thumbnailFileType;

    @Builder
    public ResThumbnailDTO(Long thumbnailId, String thumbnailFileName, String thumbnailFilePath, String thumbnailFileType) {
        this.thumbnailId = thumbnailId;
        this.thumbnailFileName = thumbnailFileName;
        this.thumbnailFilePath = thumbnailFilePath;
        this.thumbnailFileType = thumbnailFileType;
    }

    public static ResThumbnailDTO fromEntity(Thumbnail thumbnail) {
        return ResThumbnailDTO.builder()
                .thumbnailId(thumbnail.getId())
                .thumbnailFileName(thumbnail.getThumbnailOriginFileName())
                .thumbnailFilePath(thumbnail.getThumbnailFilePath())
                .thumbnailFileType(thumbnail.getThumbnailFileType())
                .build();
    }
}