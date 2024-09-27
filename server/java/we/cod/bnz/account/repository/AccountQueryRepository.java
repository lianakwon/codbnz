package we.cod.bnz.account.repository;

import we.cod.bnz.account.Account;

import java.util.Optional;

public interface AccountQueryRepository {
  Optional<Account> findByUsername(String username);
}
