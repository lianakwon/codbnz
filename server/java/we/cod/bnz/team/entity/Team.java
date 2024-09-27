package we.cod.bnz.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.team.dto.TeamDTO;
import we.cod.bnz.team.dto.TeamForm;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teams")
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "team_title")
  private String team_title;

  private String team_intro;

  // =============================

  public TeamDTO toDTO() {
    return new TeamDTO(id, team_title, team_intro);
  }

  public Team create_team(TeamForm dto) {
    return new Team(null, dto.getTeam_title(), dto.getTeam_intro());
  }

  public Team update_team(TeamForm dto) {
    return new Team(id, dto.getTeam_title(), dto.getTeam_intro());
  }

}

