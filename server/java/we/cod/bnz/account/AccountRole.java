package we.cod.bnz.account;

import lombok.Getter;

@Getter
public enum AccountRole {

  ADMIN("ROLE_ADMIN"),
  USER("ROLE_USER");
  private final String accountRole;

  AccountRole(String accountRole) {
    this.accountRole = accountRole;
  }
}
