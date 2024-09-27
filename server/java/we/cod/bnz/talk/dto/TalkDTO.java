package we.cod.bnz.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.dto.AccountDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TalkDTO {

  private Long id;

  private AccountDTO from;

  private AccountDTO to;

}
