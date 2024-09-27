package we.cod.bnz.account.oauth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.AccountRole;

import java.time.LocalDateTime;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuthAttr {
  private Map<String, Object> attributes;
  private String email;
  private String name;
  private String profileIMG;
  private String provider;

  public static OAuthAttr of(String provider,
                             Map<String, Object> attributes) {
    System.out.println("OAuthAttr : of");
    switch (provider) {
      case "google":
        return ofGoogle(attributes);
      case "kakao":
        return ofKakao(attributes);
      case "naver":
        return ofNaver(attributes);
      default:
        throw new RuntimeException();
    }
  }

  private static OAuthAttr ofGoogle(Map<String, Object> attr) {
    System.out.println("OAuthAttr : ofGoogle");
    return OAuthAttr.builder()
            .name(String.valueOf(attr.get("name")))
            .email(String.valueOf(attr.get("email")))
            .profileIMG(String.valueOf(attr.get("picture")))
            .attributes(attr)
            .provider("google")
            .build();
  }


  private static OAuthAttr ofNaver(Map<String, Object> attr) {
    System.out.println("OAuthAttr : ofNaver");
    Map<String, Object> response = (Map<String, Object>) attr.get("response");
    return OAuthAttr.builder()
            .name(String.valueOf(response.get("name")))
            .email(String.valueOf(response.get("email")))
            .profileIMG(String.valueOf(response.get("profile_image")))
            .attributes(response)
            .provider("naver")
            .build();
  }


  private static OAuthAttr ofKakao(Map<String, Object> attr) {
    System.out.println("OAuthAttr : ofKakao");
    Map<String, Object> kakaoAccount = (Map<String, Object>) attr.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuthAttr.builder()
            .name(String.valueOf(kakaoProfile.get("nickname")))
            .email(String.valueOf(kakaoAccount.get("email")))
            .profileIMG(String.valueOf(kakaoProfile.get("profile_image_url")))
            .attributes(kakaoAccount)
            .provider("kakao")
            .build();
  }

  public Account toEntity() {
    System.out.println("OAuthAttr : toEntity");
    return Account.builder()
            .username(this.getEmail())
            .password(null)
            .role(AccountRole.USER)
            .nickname(this.getName())
            .email(this.getEmail())
            .phone(null)
            .create_date(LocalDateTime.now())
            .shape("Default").color("Green").eye("Normal").face("Smile")
            .profileMSG(null)
            .provider(this.provider)
            .build();
  }
}
