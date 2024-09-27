package we.cod.bnz.account.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final AccountRepository repo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("UserDetailsServiceImpl : loadUserByUsername");
    Account account = repo.findById(Long.parseLong(username))
            .orElseThrow(() -> new UsernameNotFoundException("CANNOT FIND USER INFO"));
    return UserPrincipal.create(account);
  }
}
