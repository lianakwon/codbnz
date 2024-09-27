package we.cod.bnz.account.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import we.cod.bnz.account.AccessToken;
import we.cod.bnz.account.auth.SecretKey;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final SecretKey secretKey;
  private final String TARGET_URL = "http://localhost:3000/oauth/";


//  @Override
//  public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {
//    System.out.println("OAuthHandler : onAuthenticationSuccess");
//    UserPrincipal user = (UserPrincipal) auth.getPrincipal();
//    String token = new AccessToken(user, secretKey.getKey()).getToken();
//    getRedirectStrategy().sendRedirect(req, res, TARGET_URL + token);
//  }


  //== 신동혁 추가
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    //OAuth2User
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

//    String username = userPrincipal.getUsername();
    String token = new AccessToken(userPrincipal, secretKey.getKey()).getToken();

    response.addCookie(createCookie("Authorization", token));
    response.sendRedirect("http://localhost:3000/");
  }

  // httpOnly로 쿠키만들어 토큰에 넣고 프런트로 리다이렉트
  private Cookie createCookie(String key, String value) {

    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60*60*60);
    //cookie.setSecure(true); // 오직 https에서만 실행되게 하기위한 코드
    cookie.setPath("/");
    cookie.setHttpOnly(true);

    return cookie;
  }

}



