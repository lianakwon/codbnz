package we.cod.bnz.team.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.Account;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "members_pre")
public class MemberPre {

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

}


