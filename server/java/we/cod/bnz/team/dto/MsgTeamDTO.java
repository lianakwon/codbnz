package we.cod.bnz.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.team.entity.Team;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MsgTeamDTO {

  private String cont;

  private LocalDate date;

  private LocalTime time;

  private AccountDTO from;

  private Team team;

}
