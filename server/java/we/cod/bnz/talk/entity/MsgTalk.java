package we.cod.bnz.talk.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import we.cod.bnz.account.Account;
import we.cod.bnz.talk.dto.MsgTalkDTO;
import we.cod.bnz.talk.dto.MsgTalkForm;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "msg_talk")
public class MsgTalk {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String cont;

  @Column
  private LocalDate date;

  @Column
  private LocalTime time;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "from_id")
  private Account from;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "talk_id")
  private Talk talk;

  // =============================== //

  public MsgTalkDTO toDTO() {
    return new MsgTalkDTO(cont, date, time, from.toDTO(), talk);
  }

  public MsgTalk createMessage(MsgTalkForm dto, Account from, Talk talk) {
    return new MsgTalk(null, dto.getCont(), LocalDate.now(), LocalTime.now(), from, talk);
  }

}
