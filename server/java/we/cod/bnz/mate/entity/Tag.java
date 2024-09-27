package we.cod.bnz.mate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.dto.TagDTO;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tags")
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recruit")
  private boolean recruit; // 모집완료/모집중/전체

  @Column(name = "goal")
  private String goal; // 목적

  @Column(name = "spce")
  private String space; // 영역

//  @ElementCollection
//  @CollectionTable(name = "tag_locas", joinColumns = @JoinColumn(name = "tag_id"))
  @Column(name = "loca")// 지역
  private Set<String> locas = new HashSet<>();

//  @ElementCollection
//  @CollectionTable(name = "tag_parts", joinColumns = @JoinColumn(name = "tag_id"))
  @Column(name = "part") // 파트
  private Set<String> parts = new HashSet<>();

//  @ElementCollection
//  @CollectionTable(name = "tag_langs", joinColumns = @JoinColumn(name = "tag_id"))
  @Column(name = "lang") // 언어
  private Set<String> langs = new HashSet<>();

//  @ElementCollection
//  @CollectionTable(name = "tag_programs", joinColumns = @JoinColumn(name = "tag_id"))
  private Set<String> programs = new HashSet<>();

  @JoinColumn(name = "mate_id", referencedColumnName = "id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Mate mate; // 게시글 엔티티 조인



  public Tag(boolean recruit, String goal, String space, Set<String> locas, Set<String> parts, Set<String> langs, Set<String> programs , Mate mate) {
    this.recruit = recruit;
    this.goal = goal;
    this.space = space;
    this.locas = locas;
    this.parts = parts;
    this.langs = langs;
    this.programs = programs;
    this.mate = mate;
  }

  public Tag(TagDTO dto, Mate mate) {
    this.recruit = dto.isRecruit();
    this.goal = dto.getGoal();
    this.space = dto.getSpace();
    this.locas = dto.getLocas();
    this.parts = dto.getParts();
    this.langs = dto.getLangs();
    this.programs = dto.getPrograms();
    this.mate = mate;
  }

  public TagDTO toDTO() {
    return new TagDTO(this.id, recruit, goal, space, locas, parts, langs, programs);
  }

  // TagDTO를 사용하여 Tag 엔티티 업데이트
  public void updateTag(TagDTO tagDTO) {
    this.recruit = tagDTO.isRecruit();
    this.goal = tagDTO.getGoal();
    this.space = tagDTO.getSpace();
    this.locas = new HashSet<>(tagDTO.getLocas());
    this.parts = new HashSet<>(tagDTO.getParts());
    this.langs = new HashSet<>(tagDTO.getLangs());
    this.programs = new HashSet<>(tagDTO.getPrograms());
  }
}
