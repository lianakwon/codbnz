package we.cod.bnz.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.Account;
import we.cod.bnz.team.dto.MemberDTO;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 팀 테이블 연결
  @JoinColumn(name = "team_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Team team;

  // 어카운트 테이블 연결
  @JoinColumn(name = "account_id")
  @ManyToOne(fetch = FetchType.EAGER)
  private Account account;

  // 멤버 계정 권한 세팅
  @Column(name = "member_role")
  @Enumerated(EnumType.STRING)
  private MemberRole memberRole;


  public boolean isManager() {
    return this.memberRole.equals(MemberRole.MANAGER);
  }

  public boolean isMember() {
    return this.memberRole.equals(MemberRole.MEMBER);
  }

  public MemberDTO toDTO() {
    return new MemberDTO(team.toDTO(), account.toDTO());
  }

  public Member create_manager(Team team, Account account, MemberRole role) {
    return new Member(null, team, account, MemberRole.MANAGER);
  }

  public Member create_member(Team team, Account account, MemberRole role) {
    return new Member(null, team, account, MemberRole.MEMBER);
  }
}


