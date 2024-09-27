package we.cod.bnz.account.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final AccountRepository repo;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
    System.out.println("OAuthService : loadUser");
    // 외부 OAuth 2.0 프로바이더로부터 사용자 정보를 가져옵니다.
    OAuth2User user = new DefaultOAuth2UserService().loadUser(req);
    // 네이버, 구글, 카카오 중 어떤 것의 id인지 파악하기 위해 가져온다.
    String regId = req.getClientRegistration().getRegistrationId();
    // OAuthAttr 객체를 사용하여 OAuth 사용자의 속성을 추출합니다.
    OAuthAttr attr = OAuthAttr.of(regId, user.getAttributes());
    // 사용자를 저장하거나 업데이트하고 해당 사용자를 반환합니다.
    Account account = saveOrUpdate(attr);
    // UserPrincipal 객체를 사용하여 사용자의 principal을 생성합니다.
    return UserPrincipal.create(account, attr.getAttributes());
  }

  // 사용자를 저장하거나 업데이트하는 메서드
  public Account saveOrUpdate(OAuthAttr attr) {
    System.out.println("OAuthService : saveOrUpdate");
    // 이메일과 OAuth 프로바이더로 사용자를 찾거나 새로운 사용자를 생성합니다.
    Account account = repo.findByUsernameAndProvider(attr.getEmail(), attr.getProvider()).orElse(attr.toEntity());
    // 사용자를 저장하고 반환합니다.
    return repo.save(account);
  }

}

//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//  @Override
//  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//    // 1. DefaultOAuth2UserService를 생성하여 OAuth 2.0 사용자 서비스를 가져옴
//    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
//
//    // 2. 실제 사용자 정보를 가져옴
//    OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
//
//    // 3. 사용자 요청에서 OAuth 2.0 클라이언트 등록 정보와 사용자 이름 속성을 추출하여 로깅
//    String registrationId = userRequest.getClientRegistration().getRegistrationId();
//    // provider에서 사용자 이름 속성을 추출하는 과정
//    String userNameAttributeName = userRequest.getClientRegistration()
//            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//    log.info("registrationId = {}", registrationId);
//    log.info("userNameAttributeName = {}", userNameAttributeName);
//
//    // 4. OAuth2Attribute 객체를 생성하여 사용자 정보에서 필요한 속성을 추출
//    OAuth2Attribute oAuth2Attribute =
//            OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//    // OAuth2Attribute 객체를 사용하여 사용자 속성을 맵으로 변환
//    var memberAttribute = oAuth2Attribute.convertToMap();
//
//    // DefaultOAuth2User 객체를 생성하여 반환
//    return new DefaultOAuth2User(
//            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
//            memberAttribute, "email");
//  }
//}
