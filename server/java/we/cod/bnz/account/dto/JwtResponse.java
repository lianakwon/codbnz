package we.cod.bnz.account.dto;

import jakarta.servlet.http.Cookie;
import lombok.Data;

@Data
public class JwtResponse {

  private String accessToken, refreshToken, cookie;

}
