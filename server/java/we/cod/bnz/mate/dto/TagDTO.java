package we.cod.bnz.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.entity.Tag;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

  private Long id; // 태그 ID
  private boolean recruit; // 모집 상태
  private String goal; // 목적
  private String space; // 영역
  private Set<String> locas; // 위치
  private Set<String> parts; // 부문
  private Set<String> langs; // 언어
  private Set<String> programs; // 사용프로그램


  public TagDTO(Tag tag) {
    this.id = tag.getId();
    this.recruit = tag.isRecruit();
    this.goal = tag.getGoal();
    this.space = tag.getSpace();
    this.locas = tag.getLocas();
    this.parts = tag.getParts();
    this.langs = tag.getLangs();
    this.programs = tag.getPrograms();
  }

  // TagDTO를 사용하여 Tag 엔티티 업데이트
  public void updateTag(TagDTO tagDTO) {
    this.recruit = tagDTO.isRecruit();
    this.goal = tagDTO.getGoal();
    this.space = tagDTO.getSpace();
    this.locas = tagDTO.getLocas();
    this.parts = tagDTO.getParts();
    this.langs = tagDTO.getLangs();
    this.programs = tagDTO.getPrograms();
  }

}
