package we.cod.bnz.account.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.AccountService;
import we.cod.bnz.account.dto.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
  private final AccountService service;

  @PostMapping("/check_username")
  public String check(@RequestBody CheckRequest req) {
    return (service.checkUsername(req.getName()) == null) ? "success" : "fail";
  }

  @PostMapping("/check_nickname")
  public String checkNickname(@RequestBody CheckRequest req) {
    return (service.checkNickname(req.getName()) == null) ? "success" : "fail";
  }

  @PostMapping("/join")
  public AccountDTO join(@RequestBody JoinRequest req) {
    return service.join(req).toDTO();
  }

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody LoginRequest req) {
    return service.login(req);
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    Arrays.stream(request.getCookies())
            .forEach(cookie -> {
              cookie.setMaxAge(0);
              cookie.setValue("");
              cookie.setPath("/");
              response.addCookie(cookie);
            });
  }
}
