package we.cod.bnz.account.repository;

import we.cod.bnz.account.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static we.cod.bnz.account.QAccount.account;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepositoryImpl implements AccountQueryRepository {

  private final JPAQueryFactory factory;

  @Override
  public Optional<Account> findByUsername(String username) {
    System.out.println("AccountQueryRepositoryImpl : findByUsername");
    Account result = factory
            .selectFrom(account)
            .where(account.username.eq(username))
            .fetchOne();
    return Optional.ofNullable(result);
  }
}
