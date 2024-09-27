package we.cod.bnz.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MateListDTO {

    private String title; // 제목
    private String content; // 내용
    private LocalDateTime create_date; // 작성일시
    private LocalDateTime update_date; // 수정일시
    private TagDTO tag; // 태그 리스트
}
