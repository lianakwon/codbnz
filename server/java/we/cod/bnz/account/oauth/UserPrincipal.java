package we.cod.bnz.account.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import we.cod.bnz.account.Account;

import java.util.*;

@AllArgsConstructor
@Builder
@ToString(of = {"username", "name"})
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

  private Long username;
  private String name;
  private Map<String, Object> attr;

  public static UserPrincipal create(Account account,
                                     Map<String, Object> attr) {
    return new UserPrincipal(account.getId(), account.getUsername(), attr);
  }

  public static UserPrincipal create(Account account) {
    return new UserPrincipal(account.getId(), account.getUsername(), new HashMap<>());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorityList = new ArrayList<>();
    authorityList.add(new SimpleGrantedAuthority("USER"));
    return authorityList;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return String.valueOf(this.username);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

//  @Override
//  @Nullable
//  public <A> A getAttribute(String nickname) {
//    return (A) attr.get(nickname);
//  }


  @Override
  public Map<String, Object> getAttributes() {
    return Collections.unmodifiableMap(attr);
  }
}
