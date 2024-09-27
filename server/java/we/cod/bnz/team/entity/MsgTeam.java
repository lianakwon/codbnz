package we.cod.bnz.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.Account;
import we.cod.bnz.team.dto.MsgTeamDTO;
import we.cod.bnz.team.dto.MsgTeamForm;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "msg_team")
public class MsgTeam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String cont;

  @Column
  private LocalDate date;

  @Column
  private LocalTime time;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "from_id")
  private Account from;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "team_id")
  private Team team;

  // =============================== //

  public MsgTeamDTO toDTO() {
    return new MsgTeamDTO(cont, date, time, from.toDTO(), team);
  }

  public MsgTeam createMessage(MsgTeamForm dto, Account from, Team team) {
    return new MsgTeam(null, dto.getCont(), LocalDate.now(), LocalTime.now(), from, team);
  }

}
