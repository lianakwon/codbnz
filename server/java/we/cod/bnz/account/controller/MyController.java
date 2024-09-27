package we.cod.bnz.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.account.Account;
import we.cod.bnz.mate.dto.CommentMateDTO;
import we.cod.bnz.mate.dto.MateDTO;
import we.cod.bnz.account.MyService;
import we.cod.bnz.account.dto.ChangeIMG;
import we.cod.bnz.account.dto.ChangeTXT;
import we.cod.bnz.team.dto.TeamDTO;
import we.cod.bnz.today.dto.response.board.ResTodayListDTO;
import we.cod.bnz.today.dto.response.comment.ResCommentTodayDTO;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {


  // Repository

  private final MyService service;


  // 메인

  @GetMapping("/boards_m")
  public List<MateDTO> main_boards_m() {
    System.out.println("MyController : main_boards_m");
    return service.main_boards_m();
  }

  @GetMapping("/boards_t")
  public List<ResTodayListDTO> main_boards_t() {
    System.out.println("MyController : main_boards_t");
    return service.main_boards_t();
  }


  // 어카운트 / 프로필

  @GetMapping("/get_account/{id}")
  public Account get_account(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : change_txt");
    return service.get_account(account_id);
  }

  @PostMapping("/change_txt/{id}")
  public Account change_txt(@PathVariable(name = "id") Long account_id,
                            @RequestBody ChangeTXT req) {
    System.out.println("MyController : change_txt");
    return service.change_txt(account_id, req);
  }

  @PostMapping("/change_img/{id}")
  public Account change_img(@PathVariable(name = "id") Long account_id,
                            @RequestBody ChangeIMG req) {
    System.out.println("MyController : change_img");
    return service.change_img(account_id, req);
  }


  // 팀 / 멤버

  @GetMapping("/get_teams/{id}")
  public List<TeamDTO> my_get_teams(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_get_teams");
    return service.my_get_teams(account_id);
  }

  @GetMapping("/get_invites/{id}")
  public List<TeamDTO> my_get_invites(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_get_invites");
    return service.my_get_invites(account_id);
  }


  // 작성글 / 작성댓글

  @GetMapping("/boards_m/{id}")
  public List<MateDTO> my_boards_m(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_boards_m");
    return service.my_boards_m(account_id);
  }

  @GetMapping("/boards_t/{id}")
  public List<ResTodayListDTO> my_boards_t(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_boards_t");
    return service.my_boards_t(account_id);
  }

  @GetMapping("/comments_m/{id}")
  public List<CommentMateDTO> my_comments_m(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_comments_m");
    System.out.println(service.my_comments_m(account_id).get(0).getContent());
    return service.my_comments_m(account_id);
  }

  @GetMapping("/comments_t/{id}")
  public List<ResCommentTodayDTO> my_comments_t(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_comments_t");
    return service.my_comments_t(account_id);
  }


  // 좋아요 / 궁금해요

  @GetMapping("/likes_m/{id}")
  public List<MateDTO> my_likes_m(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_likes_m");
    return service.my_likes_m(account_id);
  }

  @GetMapping("/likes_comments_m/{id}")
  public List<CommentMateDTO> my_likes_comments_m(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_likes_comments_m");
    return service.my_likes_comments_m(account_id);
  }

  @GetMapping("/likes_t/{id}")
  public List<ResTodayListDTO> my_likes_t(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_likes_t");
    return service.my_likes_t(account_id);
  }

  @GetMapping("/likes_comments_t/{id}")
  public List<ResCommentTodayDTO> my_likes_comments_t(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_likes_comments_t");
    return service.my_likes_comments_t(account_id);
  }

  @GetMapping("/quests_t/{id}")
  public List<ResTodayListDTO> my_quests_t(@PathVariable(name = "id") Long account_id) {
    System.out.println("MyController : my_quests_t");
    return service.my_quests_t(account_id);
  }

}
