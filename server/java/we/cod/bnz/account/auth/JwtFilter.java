package we.cod.bnz.account.auth;

import jakarta.servlet.http.Cookie;
import we.cod.bnz.account.AccessToken;
import we.cod.bnz.account.oauth.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

  // HTTP 요청 헤더에서 Authorization 헤더의 이름
  public static final String AUTHORIZATION_HEADER = "Authorization";
  // 토큰의 접두사
  public static final String BEARER_PREFIX = "Bearer ";

  // 서명 키를 제공하는 SecretKey 클래스
  private final SecretKey secretKey;

  // 사용자 정보를 로드하는 서비스
  private final UserDetailsService service;


  // oauth2를 사용한 방식은 OAuthHandler에서 만약 로그인을 성공하면 cookie에 token을 담아 jwt를 보내주기 때문에
  private String getCookieToken(HttpServletRequest request) {

    //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
    String authorization = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {

        System.out.println(cookie.getName());
        if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
          authorization = cookie.getValue();
        }
      }
    }
    return authorization;
  }

  // 요청에서 JWT 토큰을 추출하는 메서드
  private String resolveToken(HttpServletRequest req) {
    System.out.println("JwtFilter : resolveToken");
    // Authorization 헤더에서 액세스 토큰을 가져옴
    String accessToken = req.getHeader(AUTHORIZATION_HEADER);
    // accessToken이 "null" 문자열이 아닌지 확인
    if ("Bearer null".equals(accessToken)) {
      accessToken = null;
    }
    // 액세스 토큰이 존재하고 Bearer 접두사로 시작하는지 확인
    if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
      // Bearer 접두사를 제거하고 토큰 부분만 반환
      return accessToken.split(" ")[1].trim();
    }
    // 토큰이 없으면 null 반환
    return null;
  }

  // 필터링 작업을 수행하는 메서드
  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain filter) throws ServletException, IOException {
    System.out.println("JwtFilter : doFilterInternal");

    // 요청에서 토큰을 추출
    String headerToken = resolveToken(req);
    String cookieToken = getCookieToken(req);
    // header 토큰을 가져오는데 없으면 쿠키에서 토큰을 검사해서 가져오기
    String token = (headerToken != null) ? headerToken : cookieToken;

    String requestURI = req.getRequestURI();

    if (token != null) {
      // 추출한 토큰과 서명 키로 AccessToken 객체를 생성
      AccessToken accessToken = new AccessToken(token, secretKey.getKey());

      // 토큰에서 클레임 데이터를 추출
      Claims claims = accessToken.getData();

      // 사용자 이름으로 사용자 정보를 로드
      UserPrincipal user = (UserPrincipal) service.loadUserByUsername(claims.getSubject());

      // 사용자 인증 객체를 생성 (권한: USER)
      Authentication auth = new UsernamePasswordAuthenticationToken(
              user, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

      // SecurityContextHolder에 인증 객체 설정
      SecurityContextHolder.getContext().setAuthentication(auth);
      // 다음 필터를 호출하여 필터 체인 계속 진행

    }
    filter.doFilter(req, res);
  }


}
