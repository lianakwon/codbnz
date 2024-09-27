package we.cod.bnz.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.cod.bnz.talk.*;
import we.cod.bnz.talk.dto.MsgTalkDTO;
import we.cod.bnz.talk.dto.MsgTalkForm;
import we.cod.bnz.talk.dto.TalkDTO;
import we.cod.bnz.talk.dto.TalkForm;
import we.cod.bnz.talk.entity.Talk;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/talk")
public class TalkController {

  private final TalkService service;

  // 채팅방 목록
  @GetMapping({"/{from}"})
  public List<TalkDTO> get_talks(@PathVariable(name = "from") Long from_id) {
    return service.get_talks(from_id);
  }

  // 채팅방 생성
  @PostMapping("/{from}")
  public TalkDTO create_talk(@PathVariable(name = "from") Long from_id,
                          @RequestBody TalkForm dto) {
    return service.create_talk(from_id, dto);
  }

  // 채팅방 삭제
  @DeleteMapping("/{from}/{talk}")
  public boolean delete_talk(@PathVariable(name = "from") Long from_id,
                             @PathVariable(name = "talk") Long talk_id) {
    return service.delete_talk(from_id, talk_id);
  }

  // 메세지 조회
  @GetMapping("/msg/{from}")
  public List<MsgTalkDTO> get_messages(@PathVariable(name = "from") Long from_id) {
    return service.get_messages(from_id);
  }

  // 메세지 생성
  @PostMapping("/msg/{from}/{talk}")
  public MsgTalkDTO create_message(@PathVariable(name = "from") Long from_id,
                                   @PathVariable(name = "talk") Long talk_id,
                                   @RequestBody MsgTalkForm dto) {
    System.out.println("TalkController : create_message");
    return service.create_message(from_id, talk_id, dto);
  }

  // 메세지 삭제
  @DeleteMapping("/msg/{from}/{msg}")
  public boolean delete_message(@PathVariable(name = "from") Long from_id,
                                @PathVariable(name = "msg") Long message_id) {
    return service.delete_message(from_id, message_id);
  }

}
