package we.cod.bnz.account;

import we.cod.bnz.account.oauth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class AccessToken {

  public static final int EXPIRED_AFTER = 300;
  private final String token;
  private final Key key;
  private LocalDateTime expiredAt;

  public AccessToken(UserPrincipal user,
                     Key key) {
    System.out.println("AccessToken : AccessToken 생성용");
    LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_AFTER);
    Date expired = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

    Map<String, String> claims = new HashMap<>();
    claims.put("iss", "admin");
    claims.put("aud", user.getUsername().toString());
    claims.put("exp", LocalDateTime.now().toString());

    this.key = key;
    this.expiredAt = expiredAt;
    this.token = createJwtAuthToken(user.getUsername().toString(), claims, expired).get();
  }

  public Optional<String> createJwtAuthToken(String username,
                                             Map<String, String> claims,
                                             Date expired) {
    System.out.println("AccessToken : createJwtAuthToken");
    return Optional.ofNullable(Jwts.builder()
            .setSubject(username)
            .addClaims(new DefaultClaims(claims))
            .signWith(key, SignatureAlgorithm.HS256)
            .setExpiration(expired)
            .compact());
  }

  public AccessToken(String token,
                     Key key) {
    System.out.println("AccessToken : AccessToken 확인용");
    this.token = token;
    this.key = key;
  }

  public Claims getData() throws ExpiredJwtException {
    System.out.println("AccessToken : getData");
    return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
  }
}
