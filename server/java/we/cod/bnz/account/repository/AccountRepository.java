package we.cod.bnz.account.repository;

import we.cod.bnz.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountQueryRepository {

  Optional<Account> findByUsernameAndProvider(String username, String provider);

  Optional<Account> findAccountByUsername(String username);

  Optional<Account> findByNickname(String nickname);

}
