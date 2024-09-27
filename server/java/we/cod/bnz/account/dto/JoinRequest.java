package we.cod.bnz.account.dto;

import lombok.Data;

@Data
public class JoinRequest {

  private String username, email, password1, password2, nickname, phone1, phone2, phone3;

}
