package we.cod.bnz.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.team.entity.Team;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {

  private Long id;

  private String team_title;

  private String team_intro;

}
