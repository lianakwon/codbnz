package we.cod.bnz.account;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.dto.ChangeIMG;
import we.cod.bnz.account.dto.ChangeTXT;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.mate.dto.CommentMateDTO;
import we.cod.bnz.mate.dto.MateDTO;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.mate.repository.CommentMateRepository;
import we.cod.bnz.mate.repository.MateRepository;
import we.cod.bnz.team.entity.Team;
import we.cod.bnz.team.dto.TeamDTO;
import we.cod.bnz.team.repository.TeamRepository;
import we.cod.bnz.today.dto.response.board.ResTodayListDTO;
import we.cod.bnz.today.dto.response.comment.ResCommentTodayDTO;
import we.cod.bnz.today.entity.CommentToday;
import we.cod.bnz.today.entity.Today;
import we.cod.bnz.today.repository.CommentTodayRepository;
import we.cod.bnz.today.repository.TodayRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MyService {


  // Repository

  private final AccountRepository repoA;
  private final MateRepository repoM;
  private final CommentMateRepository repoMC;
  private final TodayRepository repoT;
  private final CommentTodayRepository repoTC;
  private final TeamRepository repoTeam;


  // 메인

  public List<MateDTO> main_boards_m() {
    System.out.println("MyService : main_boards_m");
    List<Mate> list = repoM.main_boards_mate();
    return list.stream().map(Mate::toDTO).collect(Collectors.toList());
  }

  public List<ResTodayListDTO> main_boards_t() {
    System.out.println("MyService : main_boards_t");
    List<Today> list = repoT.main_boards_today();
    return list.stream().map(ResTodayListDTO::fromEntity).collect(Collectors.toList());
  }


  // 어카운트 / 프로필

  public Account get_account(Long account_id) {
    System.out.println("MyService : get_account");
    if (check_account(account_id) == null) return null;
    return check_account(account_id);
  }

  public Account change_txt(Long account_id,
                            ChangeTXT req) {
    System.out.println("MyService : change_txt");
    if (check_account(account_id) == null) return null;
    Account account = check_account(account_id).changeTXT(req);
    repoA.save(account);
    return account;
  }

  public Account change_img(Long account_id,
                            ChangeIMG req) {
    System.out.println("MyService : change_img");
    if (check_account(account_id) == null) return null;
    Account account = check_account(account_id).changeIMG(req);
    repoA.save(account);
    return account;
  }


  // 팀 / 멤버

  public List<TeamDTO> my_get_teams(Long account_id) {
    System.out.println("MyService : my_get_teams");
    if (check_account(account_id) == null) return null;
    return repoTeam.find_by_from(account_id).stream().map(Team::toDTO).collect(Collectors.toList());
  }

  public List<TeamDTO> my_get_invites(Long account_id) {
    System.out.println("MyService : my_get_invites");
    if (check_account(account_id) == null) return null;
    return repoTeam.get_invites(account_id).stream().map(Team::toDTO).collect(Collectors.toList());
  }


  // 작성글 / 작성댓글

  public List<MateDTO> my_boards_m(Long account_id) {
    System.out.println("MyService : my_boards_m");
    if (check_account(account_id) == null) return null;
    List<Mate> list = repoM.get_boards_mate(account_id);
    return list.stream().map(Mate::toDTO).collect(Collectors.toList());
  }

  public List<CommentMateDTO> my_comments_m(Long account_id) {
    System.out.println("MyService : my_comments_m");
    if (check_account(account_id) == null) return null;
    List<CommentMate> list = repoMC.get_comments_mate(account_id);
    System.out.println(list.get(0).getContent());
    return list.stream().map(CommentMate::toDTO).collect(Collectors.toList());
  }

  public List<ResTodayListDTO> my_boards_t(Long account_id) {
    System.out.println("MyService : my_boards_t");
    if (check_account(account_id) == null) return null;
    List<Today> todays = repoT.get_boards_today(account_id);
    return todays.stream().map(ResTodayListDTO::fromEntity).collect(Collectors.toList());
  }

  public List<ResCommentTodayDTO> my_comments_t(Long account_id) {
    System.out.println("MyService : my_comments_t");
    if (check_account(account_id) == null) return null;
    String profile = check_account(account_id).getShape() + check_account(account_id).getColor() + check_account(account_id).getEye() + check_account(account_id).getFace();
    List<CommentToday> list = repoTC.get_comments_today(account_id);
    return list.stream().map(comment -> ResCommentTodayDTO.fromEntity(comment, profile)).collect(Collectors.toList());
  }


  // 좋아요 / 궁금해요

  public List<MateDTO> my_likes_m(Long account_id) {
    System.out.println("MyService : my_likes_m");
    if (check_account(account_id) == null) return null;
    List<Mate> list = repoM.get_likes_mate(account_id);
    return list.stream().map(Mate::toDTO).collect(Collectors.toList());
  }

  public List<CommentMateDTO> my_likes_comments_m(Long account_id) {
    System.out.println("MyService : my_likes_comments_m");
    if (check_account(account_id) == null) return null;
    List<CommentMate> list = repoMC.get_likes_comments_mate(account_id);
    return list.stream().map(CommentMate::toDTO).collect(Collectors.toList());
  }

  public List<ResTodayListDTO> my_likes_t(Long account_id) {
    System.out.println("MyService : my_likes_t");
    if (check_account(account_id) == null) return null;
    List<Today> list = repoT.get_likes_today(account_id);
    return list.stream().map(ResTodayListDTO::fromEntity).collect(Collectors.toList());
  }

  public List<ResCommentTodayDTO> my_likes_comments_t(Long account_id) {
    System.out.println("MyService : my_likes_comments_t");
    if (check_account(account_id) == null) return null;
    String profile = check_account(account_id).getShape() + check_account(account_id).getColor() + check_account(account_id).getEye() + check_account(account_id).getFace();
    List<CommentToday> list = repoTC.get_likes_comments_today(account_id);
    return list.stream().map(comment -> ResCommentTodayDTO.fromEntity(comment, profile)).collect(Collectors.toList());
  }

  public List<ResTodayListDTO> my_quests_t(Long account_id) {
    System.out.println("MyService : my_quests_t");
    if (check_account(account_id) == null) return null;
    List<Today> list = repoT.get_quests_today(account_id);
    return list.stream().map(ResTodayListDTO::fromEntity).collect(Collectors.toList());
  }


  // 체크

  public Account check_account(Long account_id) {
    return repoA.findById(account_id).orElse(null);
  }

}
