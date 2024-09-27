package we.cod.bnz.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.dto.AccountDTO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

  private TeamDTO team;

  private AccountDTO account;

}


