package we.cod.bnz.today.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.today.common.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Thumbnail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "TN_ORIGIN_FILE_NAME")
    private String thumbnailOriginFileName;

    @Column(name = "TN_FILE_TYPE")
    private String thumbnailFileType;

    @Column(name = "TN_FILE_PATH")
    private String thumbnailFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TODAY_ID")
    public Today today;

    @Builder
    public Thumbnail(Long id,
                     String thumbnailOriginFileName,
                     String thumbnailFilePath,
                     String thumbnailFileType
    ) {
        this.id = id;
        this.thumbnailOriginFileName = thumbnailOriginFileName;
        this.thumbnailFilePath = thumbnailFilePath;
        this.thumbnailFileType = thumbnailFileType;
    }
    public void setMappingToday(Today today) {
        this.today = today;
    }

}
