package we.cod.bnz.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.team.dto.MemberDTO;
import we.cod.bnz.team.dto.MsgTeamDTO;
import we.cod.bnz.team.dto.TeamDTO;
import we.cod.bnz.team.entity.MemberPre;
import we.cod.bnz.team.dto.MemberForm;
import we.cod.bnz.team.*;
import we.cod.bnz.team.entity.Team;
import we.cod.bnz.team.dto.MsgTeamForm;
import we.cod.bnz.team.dto.TeamForm;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {


  // Service

  private final TeamService service;


  // 팀

  @GetMapping("/{from}")
  public List<TeamDTO> get_teams(@PathVariable(name = "from") Long from_id) {
    return service.get_teams(from_id);
  }

  @PostMapping("/{from}/{team}")
  public Team create_team(@PathVariable(name = "from") Long from_id,
                          @RequestBody TeamForm dto) {
    return service.create_team(from_id, dto);
  }

  @PutMapping("/{from}/{team}")
  public Team update_team(@PathVariable(name = "from") Long from_id,
                          @PathVariable(name = "team") Long team_id,
                          @RequestBody TeamForm dto) {
    return service.update_team(from_id, team_id, dto);
  }

  @DeleteMapping("/{from}/{team}")
  public boolean delete_team(@PathVariable(name = "from") Long from_id,
                             @PathVariable(name = "team") Long team_id) {
    return service.delete_team(from_id, team_id);
  }


  // 채팅

  @GetMapping("/msg/{from}")
  public List<MsgTeamDTO> get_messages(@PathVariable(name = "from") Long from_id) {
    return service.get_messages(from_id);
  }

  @PostMapping("/msg/{from}/{team}")
  public MsgTeamDTO create_message(@PathVariable(name = "from") Long from_id,
                                   @PathVariable(name = "team") Long team_id,
                                   @RequestBody MsgTeamForm dto) {
    return service.create_message(from_id, team_id, dto);
  }

  @DeleteMapping("/msg/{from}/{msg}")
  public boolean delete_message(@PathVariable(name = "from") Long from_id,
                                @PathVariable(name = "msg") Long msg_id) {
    return service.delete_message(from_id, msg_id);
  }


  // 멤버

  @GetMapping("/member/get_members/{from}/{team}")
  public List<MemberDTO> get_members(@PathVariable(name = "from") Long from_id,
                                     @PathVariable(name = "team") Long team_id) {
    return service.get_members(from_id, team_id);
  }

  @PutMapping("/member/put_out_team/{from}")
  public boolean put_out_team(@PathVariable(name = "from") Long from_id,
                              @RequestBody MemberForm req) {
    return service.put_out_team(from_id, req);
  }

  @PutMapping("/get_out_team/{from}")
  public boolean get_out_team(@PathVariable(name = "from") Long from_id,
                              @RequestBody MemberForm req) {
    return service.get_out_team(from_id, req);
  }


  // 초대

  @PutMapping("/member/put_in_team/{from}")
  public MemberPre put_in_team(@PathVariable(name = "from") Long from_id,
                               @RequestBody MemberForm req) {
    return service.put_in_team(from_id, req);
  }

  @PutMapping("/member/get_in_team/{from}")
  public MemberDTO get_in_team(@PathVariable(name = "from") Long from_id,
                               @RequestBody MemberForm req) {
    return service.get_in_team(from_id, req);
  }

  @PutMapping("/member/get_in_not_team/{from}")
  public boolean get_in_not_team(@PathVariable(name = "from") Long from_id,
                                 @RequestBody MemberForm req) {
    return service.get_in_not_team(from_id, req);
  }

}
