package we.cod.bnz.talk;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.talk.dto.MsgTalkDTO;
import we.cod.bnz.talk.dto.MsgTalkForm;
import we.cod.bnz.talk.dto.TalkDTO;
import we.cod.bnz.talk.dto.TalkForm;
import we.cod.bnz.talk.entity.MsgTalk;
import we.cod.bnz.talk.entity.Talk;
import we.cod.bnz.talk.repository.MsgTalkRepository;
import we.cod.bnz.talk.repository.TalkRepository;
//import we.cod.bnz.account.Account;
//import we.cod.bnz.account.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TalkService {

  private final AccountRepository repoA;
  private final TalkRepository repoT;
  private final MsgTalkRepository repoM;

  // 채팅방 목록
  public List<TalkDTO> get_talks(Long from_id) {
    if (check_account(from_id) == null) return null;
    return repoT.find_by_from(from_id).stream().map(Talk::toDTO).collect(Collectors.toList());
  }

  // 채팅방 생성
  public TalkDTO create_talk(Long from_id,
                             TalkForm dto) {
    if (check_account(from_id) == null) return null;
    if (check_account(dto.getTo()) == null) return null;
    Talk talk = new Talk().create_talk(
            check_account(from_id),
            check_account(dto.getTo()));
    repoT.save(talk);
    return talk.toDTO();
  }

  // 채팅방 삭제
  public boolean delete_talk(Long from_id,
                             Long talk_id) {
    if (check_account(from_id) == null) return false;
    if (check_talk(talk_id) == null) return false;
    repoT.delete(check_talk(talk_id));
    return true;
  }

  // 메세지 조회
  public List<MsgTalkDTO> get_messages(Long from_id) {
    if (check_account(from_id) == null) return null;
    List<MsgTalk> list = repoM.findByFromId(from_id);
    return list.stream().map(MsgTalk::toDTO).collect(Collectors.toList());
  }

  // 메세지 생성
  public MsgTalkDTO create_message(Long from_id,
                                   Long talk_id,
                                   MsgTalkForm dto) {
    System.out.println("TalkService : create_message");
    if (check_account(from_id) == null) return null;
    if (check_talk(talk_id) == null) return null;
    MsgTalk msg = new MsgTalk().createMessage(
            dto,
            check_account(from_id),
            check_talk(talk_id));
    repoM.save(msg);
    return msg.toDTO();
  }

  // 메세지 삭제
  public boolean delete_message(Long from_id,
                                Long message_id) {
    if (check_account(from_id) == null) return false;
    if (check_message(message_id) == null) return false;
    repoM.delete(check_message(message_id));
    return true;
  }


  // Check

  public Account check_account(Long account_id) {
    return repoA.findById(account_id).orElse(null);
  }

  public Talk check_talk(Long talk_id) {
    return repoT.findById(talk_id).orElse(null);
  }

  public MsgTalk check_message(Long comment_id) {
    return repoM.findById(comment_id).orElse(null);
  }

}
