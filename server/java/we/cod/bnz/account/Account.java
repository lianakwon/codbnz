package we.cod.bnz.account;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import we.cod.bnz.account.dto.AccountDTO;
import we.cod.bnz.account.dto.ChangeIMG;
import we.cod.bnz.account.dto.ChangeTXT;
import we.cod.bnz.team.entity.Member;
import we.cod.bnz.team.dto.TeamDTO;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"username"})
@Getter
@Builder
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private AccountRole role;

  private String nickname;
  private String email;
  private String phone;

  @Column(name = "create_date")
  private LocalDateTime create_date;

  private String shape;
  private String color;
  private String eye;
  private String face;

  @Column(name = "profile_MSG")
  private String profileMSG;

  private String provider;

  // ===================================

  public AccountDTO toDTO() {
    return new AccountDTO(id, username, nickname, shape + color + eye + face + ".png", profileMSG);
  }

  public Member saveManager(TeamDTO dto) {
    return null;
  }

  public Account changeTXT(ChangeTXT req) {
    this.nickname = req.getNickname();
    this.profileMSG = req.getProfileMSG();
    System.out.println(this.toString());
    return this;
  }

  public Account changeIMG(ChangeIMG req) {
    this.shape = req.getShape();
    this.color = req.getColor();
    this.eye = req.getEye();
    this.face = req.getFace();
    System.out.println(this.toString());
    return this;
  }
}
