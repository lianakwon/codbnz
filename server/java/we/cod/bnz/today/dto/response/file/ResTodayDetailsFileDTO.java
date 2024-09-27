package we.cod.bnz.today.dto.response.file;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.entity.FileEntity;

/**
 * -Response-
 *  게시글 상세 정보에 포함될 file 정보 dto
 */

@Getter
@Setter
@NoArgsConstructor
public class ResTodayDetailsFileDTO {

    private Long fileId;
    private String originFileName;
    private String fileType;

    @Builder
    public ResTodayDetailsFileDTO(Long fileId, String originFileName, String fileType) {
        this.fileId = fileId;
        this.originFileName = originFileName;
        this.fileType = fileType;
    }

    public static ResTodayDetailsFileDTO fromEntity(FileEntity file) {
        return ResTodayDetailsFileDTO.builder()
                .fileId(file.getId())
                .originFileName(file.getOriginFileName())
                .fileType(file.getFileType())
                .build();
    }
}
