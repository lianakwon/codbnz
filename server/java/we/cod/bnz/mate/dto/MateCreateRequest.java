package we.cod.bnz.mate.dto;

import lombok.*;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.entity.Tag;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MateCreateRequest {
    private String username;
    private String title;
    private String content;
    private MateCategory Category; // 스터디 OR 프로젝트
    private String type; // 온라인 OR 오프라인
    private List<Tag> tags; // 태그 리스트
}
