package we.cod.bnz.account.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import we.cod.bnz.account.oauth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.talk.TalkService;
import we.cod.bnz.team.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  // AuthTest
  @GetMapping("/test")
  public Map<String, Object> authTest(@AuthenticationPrincipal UserPrincipal user) {
    Map<String, Object> map = new HashMap<>();
    map.put("authTest", user);
    return map;
  }
}
