package we.cod.bnz.team;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.team.dto.MemberDTO;
import we.cod.bnz.team.dto.MsgTeamDTO;
import we.cod.bnz.team.dto.TeamDTO;
import we.cod.bnz.team.entity.*;
import we.cod.bnz.team.dto.MemberForm;
import we.cod.bnz.team.dto.MsgTeamForm;
import we.cod.bnz.team.dto.TeamForm;
import we.cod.bnz.team.repository.MemberPreRepository;
import we.cod.bnz.team.repository.MemberRepository;
import we.cod.bnz.team.repository.MsgTeamRepository;
import we.cod.bnz.team.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {


  // Repository

  private final AccountRepository repoA;
  private final TeamRepository repoT;
  private final MsgTeamRepository repoTM;
  private final MemberRepository repoM;
  private final MemberPreRepository repoMP;


  // 팀

  public List<TeamDTO> get_teams(Long id) {
    return repoT.find_by_from(id).stream().map(Team::toDTO).collect(Collectors.toList());
  }

  public Team create_team(Long id,
                          TeamForm dto) {
    if (check_account(id) == null) return null;
    Team team = new Team().create_team(dto);
    repoT.save(team);
    Member manager = new Member().create_manager(team, check_account(id), MemberRole.MANAGER);
    repoM.save(manager);
    return team;
  }

  public Team update_team(Long id,
                          Long team_id,
                          TeamForm dto) {
    if (check_account(id) == null) return null;
    if (check_team(team_id) == null) return null;
    Team team = new Team().update_team(dto);
    repoT.save(team);
    return team;
  }

  public boolean delete_team(Long from_id,
                             Long team_id) {
    if (check_account(from_id) == null) return false;
    if (check_team(team_id) == null) return false;
    if (check_member(from_id, team_id) == null) return false;
    if (check_manager(from_id, team_id) == null) return false;

    List<Member> members = repoM.findByTeamId(team_id);
    List<Long> member_ids = members.stream()
            .map(mem -> mem.getAccount().getId())
            .collect(Collectors.toList());
    for (Long memberId : member_ids) System.out.println(memberId);
    repoM.deleteAllById(member_ids);
    repoT.delete(check_team(team_id));
    return true;
  }


  // 채팅

  public List<MsgTeamDTO> get_messages(Long from_id) {
    if (check_account(from_id) == null) return null;
    List<MsgTeam> messages = repoTM.findByFrom(from_id);
    if (messages.isEmpty()) return null;
    return messages.stream().map(MsgTeam::toDTO).collect(Collectors.toList());
  }

  public MsgTeamDTO create_message(Long from_id,
                                   Long team_id,
                                   MsgTeamForm dto) {
    System.out.println("TeamService : create_message");
    if (check_account(from_id) == null) return null;
    if (check_team(team_id) == null) return null;
    MsgTeam message = new MsgTeam().createMessage(dto, check_account(from_id), check_team(team_id));
    repoTM.save(message);
    return message.toDTO();
  }

  public boolean delete_message(Long from_id,
                                Long msg_id) {
    if (check_account(from_id) == null) return false;
    if (check_message(msg_id) == null) return false;
    repoM.deleteById(msg_id);
    return true;
  }


  // 멤버

  public List<MemberDTO> get_members(Long from_id,
                                     Long team_id) {
    if (check_member(from_id, team_id) == null) return null;
    return repoM.findByTeamId(team_id).stream().map(Member::toDTO).collect(Collectors.toList());
  }

  public boolean put_out_team(Long from_id,
                              MemberForm req) {
    if (check_member(from_id, req.getTeam_id()) == null) return false;
    if (check_manager(from_id, req.getTeam_id()) == null) return false;
    if (check_target(req) == null) return false;
    repoM.delete(check_target(req));
    return true;
  }

  public boolean get_out_team(Long from_id,
                              MemberForm req) {
    if (check_member(from_id, req.getTeam_id()) == null) return false;
    if (check_target(req) == null) return false;
    repoM.delete(check_target(req));
    return true;
  }


  // 초대

  public MemberPre put_in_team(Long from_id,
                               MemberForm req) {
    if (check_member(from_id, req.getTeam_id()) == null) return null;
    if (check_manager(from_id, req.getTeam_id()) == null) return null;
    if (check_account(req.getTarget_id()) == null) return null;
    if (check_team(req.getTeam_id()) == null) return null;
    MemberPre memberPre = new MemberPre(null, check_team(req.getTeam_id()), check_account(req.getTarget_id()));
    repoMP.save(memberPre);
    return memberPre;
  }

  public MemberDTO get_in_team(Long from_id,
                               MemberForm req) {
    if (check_member_pre(from_id, req.getTeam_id()) == null) return null;
    if (check_account(req.getTarget_id()) == null) return null;
    if (check_team(req.getTeam_id()) == null) return null;
    repoMP.delete(check_member_pre(req.getTarget_id(), req.getTeam_id()));
    Member member = new Member().create_member(check_team(req.getTeam_id()), check_account(req.getTarget_id()), MemberRole.MEMBER);
    repoM.save(member);
    return member.toDTO();
  }

  public boolean get_in_not_team(Long from_id,
                                 MemberForm req) {
    if (check_member_pre(from_id, req.getTeam_id()) == null) return false;
    repoMP.delete(check_member_pre(from_id, req.getTeam_id()));
    return true;
  }


  // check

  public Account check_account(Long account_id) {
    return repoA.findById(account_id).orElse(null);
  }

  public Team check_team(Long team_id) {
    return repoT.findById(team_id).orElse(null);
  }

  public MsgTeam check_message(Long msg_id) {
    return repoTM.findById(msg_id).orElse(null);
  }

  public Member check_member(Long account_id, Long team_id) {
    return repoM.findByAccountAndTeam(account_id, team_id).orElse(null);
  }

  public MemberPre check_member_pre(Long account_id, Long team_id) {
    return repoMP.findByAccountIdAndTeamId(account_id, team_id);
  }

  public Member check_target(MemberForm req) {
    return repoM.findByAccountAndTeam(req.getTarget_id(), req.getTeam_id()).orElse(null);
  }

  public Member check_manager(Long account_id, Long team_id) {
    if (MemberRole.MANAGER.equals(check_member(account_id, team_id).getMemberRole()))
      return check_member(account_id, team_id);
    else return null;
  }

}
