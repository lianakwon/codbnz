package we.cod.bnz.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.auth.SecretKey;
import we.cod.bnz.account.dto.JoinRequest;
import we.cod.bnz.account.dto.LoginRequest;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository repoA;
  private final PasswordEncoder encoder;
  private final SecretKey secretKey;

  public Account join(JoinRequest req) {
    Account a = new Account(null, req.getUsername(), encoder.encode(req.getPassword1()),
            AccountRole.USER, req.getNickname(), req.getEmail(),
            req.getPhone1() + req.getPhone2() + req.getPhone3(), LocalDateTime.now(),
            "Default", "Green", "Normal", "Smile", null, null);
    repoA.save(a);
    return a;
  }

  public Account checkUsername(String username) {
    Optional<Account> account = repoA.findAccountByUsername(username);
    return account.orElse(null);
  }

  public Account checkNickname(String nickname) {
    Optional<Account> account = repoA.findByNickname(nickname);
    return account.orElse(null);
  }

  public Map<String, String> login(LoginRequest req) {
    Account account = repoA.findByUsername(req.getUsername()).orElseThrow(() -> new UsernameNotFoundException("WRONG USERNAME"));
    if (!encoder.matches(req.getPassword(), account.getPassword())) throw new BadCredentialsException("WRONG PASSWORD");
    UserPrincipal user = UserPrincipal.create(account);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, req.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER")));
    SecurityContextHolder.getContext().setAuthentication(token);

    String accessToken = new AccessToken(user, secretKey.getKey()).getToken();
    Map<String, String> res = new HashMap<>();
    res.put("username", account.getUsername());
    res.put("nickname", account.getNickname());
    res.put("accessToken", accessToken);
    res.put("message", "로그인 성공");
    return res;
  }
}
