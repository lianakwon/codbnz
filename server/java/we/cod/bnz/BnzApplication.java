package we.cod.bnz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.AccountRole;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.mate.entity.Tag;
import we.cod.bnz.mate.repository.CommentMateRepository;
import we.cod.bnz.mate.repository.MateRepository;
import we.cod.bnz.mate.repository.TagRepository;
import we.cod.bnz.team.entity.Member;
import we.cod.bnz.team.repository.MemberRepository;
import we.cod.bnz.team.entity.MemberRole;
import we.cod.bnz.talk.entity.MsgTalk;
import we.cod.bnz.talk.repository.MsgTalkRepository;
import we.cod.bnz.talk.entity.Talk;
import we.cod.bnz.talk.repository.TalkRepository;
import we.cod.bnz.team.entity.MsgTeam;
import we.cod.bnz.team.repository.MsgTeamRepository;
import we.cod.bnz.team.entity.Team;
import we.cod.bnz.team.repository.TeamRepository;
import we.cod.bnz.today.common.AnswerStatus;
import we.cod.bnz.today.entity.CommentToday;
import we.cod.bnz.today.repository.CommentTodayRepository;
import we.cod.bnz.today.entity.Today;
import we.cod.bnz.today.repository.TodayRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@SpringBootApplication
public class BnzApplication implements CommandLineRunner {

  @Autowired
  private AccountRepository repoA;
  @Autowired
  private TeamRepository repoT;
  @Autowired
  private MemberRepository repoM;
  @Autowired
  private TalkRepository repoK;
  @Autowired
  private MsgTalkRepository repoMK;
  @Autowired
  private MsgTeamRepository repoMM;
  @Autowired
  private MateRepository repoBM;
  @Autowired
  private TagRepository repoTM;
  @Autowired
  private CommentMateRepository repoCM;
  @Autowired
  private TodayRepository repoBT;
  @Autowired
  private CommentTodayRepository repoCT;
  @Autowired
  private BCryptPasswordEncoder encoder;

  public static void main(String[] args) {
    SpringApplication.run(BnzApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    // 현재 저는 MySQL 쓰고 있습니다!!!!!!          ※ application.yml 참고
    // 어플을 가동하면 자동으로 어드민, 유저, 팀, 멤버 더미데이터 생성
    // 저와 기존 세팅이 아예 다르다면 엉망이 될 수 있으니 본인 파트는 본인 파트에 맞게 수정 후 사용!!!
    // 메이트, 투데이, 플래너, 댓글, 등 여타 더미데이터가 필요하다면 아래 양식 참고해서 추가작성하시고
    // 이전에 xml 로 만들었던 더미데이터와 다르게 id 값 지정 오류는 없으니 걱정말고 쓰세용 화이팅!!!

    Long l = 1L;
    try {

      // 어카운트.어드민이 없으면 어드민 어카운트 1개 생성
      Account admin = repoA.findById(l).orElse(null);
      if (admin == null) {
        for (Long i = l; i <= 10; i++) {
          Account a;
          if (i == 1L)
            a = new Account(null, "admin123", encoder.encode("admin123"), AccountRole.ADMIN, "어드민", "admin@gmail.com", "010-1234-1234", LocalDateTime.now(), "Round", "Red", "Small", "Smile", "코드빈즈", null);
          else if (i == 2L)
            a = new Account(null, "2", encoder.encode("2"), AccountRole.USER, "권미량", "kmr1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Big", "Grn", "Long", "Soso", "힘내보기", null);
          else if (i == 3L)
            a = new Account(null, "3", encoder.encode("3"), AccountRole.USER, "박지예", "pjy1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Dump", "Yel", "Small", "Ah", "화이팅ㅎㅎ", null);
          else if (i == 4L)
            a = new Account(null, "4", encoder.encode("4"), AccountRole.USER, "신동혁", "sdh1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Round", "Red", "Shine", "Smile", "열정", null);
          else if (i == 5L)
            a = new Account(null, "5", encoder.encode("5"), AccountRole.USER, "임건희", "lkh1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Round", "Blu", "Shine", "Oh", " ", null);
          else if (i == 6L)
            a = new Account(null, "6", encoder.encode("6"), AccountRole.USER, "권영학", "kyh1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Big", "Yel", "Long", "Smile", "힘내보기", null);
          else if (i == 7L)
            a = new Account(null, "7", encoder.encode("7"), AccountRole.USER, "우동균", "wdk1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Long", "Blu", "Big", "Ah", "열정", null);
          else if (i == 8L)
            a = new Account(null, "8", encoder.encode("8"), AccountRole.USER, "이종재", "ljj1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Round", "Red", "Small", "Oh", " ", null);
          else if (i == 9L)
            a = new Account(null, "9", encoder.encode("9"), AccountRole.USER, "정진동", "jjd1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Dump", "Grn", "Big", "Soso", "화이팅ㅎㅎ", null);
          else
            a = new Account(null, "0", encoder.encode("0"), AccountRole.USER, "한우섭", "hws1234@gmail.com", "010-1234-1234", LocalDateTime.now(), "Long", "Red", "Small", "Smile", " ", null);
          repoA.save(a);
        }
      }

      // 팀 / 팀메세지 / 멤버
      Team team = repoT.findById(l).orElse(null);
      if (team == null) {
        Team t;
        t = new Team(null, "코드빈즈", "함께 공부하실 콩, 여기여기 모여라!");
        repoT.save(t);
        t = new Team(null, "권미량팀", "권미량팀 소개입니다.");
        repoT.save(t);
        t = new Team(null, "박지예팀", "박지예팀 소개입니다.");
        repoT.save(t);
        t = new Team(null, "신동혁팀", "신동혁팀 소개입니다.");
        repoT.save(t);
        t = new Team(null, "임건희팀", "임건희팀 소개입니다.");
        repoT.save(t);
        for (Long i = l; i <= 5L; i++) {
          Member mb;
          mb = new Member(null, repoT.findById(i).orElse(null), repoA.findById(i).orElse(null), MemberRole.MANAGER);
          repoM.save(mb);
          mb = new Member(null, repoT.findById(i).orElse(null), repoA.findById(i + 1).orElse(null), MemberRole.MANAGER);
          repoM.save(mb);
          mb = new Member(null, repoT.findById(i).orElse(null), repoA.findById(i + 2).orElse(null), MemberRole.MANAGER);
          repoM.save(mb);
          mb = new Member(null, repoT.findById(i).orElse(null), repoA.findById(i + 3).orElse(null), MemberRole.MANAGER);
          repoM.save(mb);
        }
        for (Long i = l; i <= 5; i++) {
          Member mb;
          MsgTeam tm;
          tm = new MsgTeam(null, "제로게임 시~작!", LocalDate.now(), LocalTime.now(), repoA.findById(1L).orElse(null), repoT.findById(i).orElse(null));
          repoMM.save(tm);
          tm = new MsgTeam(null, i.toString(), LocalDate.now(), LocalTime.now(), repoA.findById(2L).orElse(null), repoT.findById(i).orElse(null));
          repoMM.save(tm);
          tm = new MsgTeam(null, i.toString(), LocalDate.now(), LocalTime.now(), repoA.findById(3L).orElse(null), repoT.findById(i).orElse(null));
          repoMM.save(tm);
          tm = new MsgTeam(null, i.toString(), LocalDate.now(), LocalTime.now(), repoA.findById(4L).orElse(null), repoT.findById(i).orElse(null));
          repoMM.save(tm);
          tm = new MsgTeam(null, i.toString(), LocalDate.now(), LocalTime.now(), repoA.findById(5L).orElse(null), repoT.findById(i).orElse(null));
          repoMM.save(tm);
        }
      }

      // 톡 / 톡메세지
      Talk talk = repoK.findById(l).orElse(null);
      if (talk == null) {
        for (Long i = l; i <= 5L; i++) {
          Talk k;
          k = new Talk(null, repoA.findById(i).orElse(null), repoA.findById(11 - i).orElse(null));
          repoK.save(k);
        }
        for (Long i = l; i < 6L; i++) {
          MsgTalk msg1, msg2;
          msg1 = new MsgTalk(null, "제발", LocalDate.now(), LocalTime.now(), repoK.findById(i).get().getFrom(), repoK.findById(i).orElse(null));
          msg2 = new MsgTalk(null, "돼라", LocalDate.now(), LocalTime.now(), repoK.findById(i).get().getTo(), repoK.findById(i).orElse(null));
          repoMK.save(msg1);
          repoMK.save(msg2);
        }
      }

      // 메이트
      Mate mate = repoBM.findById(l).orElse(null);
      if (mate == null) {
        for (Long i = l; i < 3; i++) {
          Mate bm;
          Tag tag;
          bm = Mate.builder().title("같이 공부하실 콩?")
                  .content("같이 공부하실 콩, 여기여기 붙어라! 코드빈즈에서 여러분을 기다립니다! 댓글 주시면 일대일 채팅으로 찾아갈게요.")
                  .author(repoA.findById(2L).orElse(null)).create_date(LocalDateTime.now()).update_date(LocalDateTime.now()).hits(0L).build();
          repoBM.save(bm);
          tag = new Tag(
                  true, // 모집 상태를 기본값으로 설정 (예: true)
                  (i % 2 == 0) ? "PROJECT" : "STUDY", // 목표(goal)을 PROJECT와 STUDY로 번갈아 설정
                  (i % 2 == 0) ? "ONLINE" : "OFFLINE", // 공간(space)을 ONLINE과 OFFLINE으로 번갈아 설정
                  new HashSet<>(Arrays.asList("loca 1", "loca 2", "loca 3")), // 예시 위치 데이터
                  new HashSet<>(Arrays.asList("part 1", "part 2", "part 3")), // 예시 부문 데이터
                  new HashSet<>(Arrays.asList("lang 1", "lang 2", "lang 3")), // 예시 언어 데이터
                  new HashSet<>(Arrays.asList("Photoshop", "Illustrator", "MySQL", "MariaDB")), // 예시 프로그램 데이터
                  bm // 생성된 Mate 객체와 연관
          );
          repoTM.save(tag);

          bm = Mate.builder().title("매주 토요일 오후 2-6시 같이 스터디 하실 분!")
                  .content("매주 토요일마다 같이 스터디 하실 분 구합니다. 저는 UI·UX 디자이너이고, 함께 고민하면서 성장할 분 채팅주세요.")
                  .author(repoA.findById(3L).orElse(null)).create_date(LocalDateTime.now()).update_date(LocalDateTime.now()).hits(0L).build();
          repoBM.save(bm);
          tag = new Tag(
                  true, // 모집 상태를 기본값으로 설정 (예: true)
                  (i % 2 == 0) ? "PROJECT" : "STUDY", // 목표(goal)을 PROJECT와 STUDY로 번갈아 설정
                  (i % 2 == 0) ? "ONLINE" : "OFFLINE", // 공간(space)을 ONLINE과 OFFLINE으로 번갈아 설정
                  new HashSet<>(Arrays.asList("loca 1", "loca 2", "loca 3")), // 예시 위치 데이터
                  new HashSet<>(Arrays.asList("part 1", "part 2", "part 3")), // 예시 부문 데이터
                  new HashSet<>(Arrays.asList("lang 1", "lang 2", "lang 3")), // 예시 언어 데이터
                  new HashSet<>(Arrays.asList("Photoshop", "Illustrator", "MySQL", "MariaDB")), // 예시 프로그램 데이터
                  bm // 생성된 Mate 객체와 연관
          );
          repoTM.save(tag);

          bm = Mate.builder().title("매주 줌으로 인강 함께 봐요")
                  .content("혼자 보려니까 집중을 못해서...ㅎㅎㅎ 매주 줌으로 인강 함께 보면서 으쌰으쌰하실 분들 채팅 주세요.")
                  .author(repoA.findById(4L).orElse(null)).create_date(LocalDateTime.now()).update_date(LocalDateTime.now()).hits(0L).build();
          repoBM.save(bm);
          tag = new Tag(
                  true, // 모집 상태를 기본값으로 설정 (예: true)
                  (i % 2 == 0) ? "PROJECT" : "STUDY", // 목표(goal)을 PROJECT와 STUDY로 번갈아 설정
                  (i % 2 == 0) ? "ONLINE" : "OFFLINE", // 공간(space)을 ONLINE과 OFFLINE으로 번갈아 설정
                  new HashSet<>(Arrays.asList("loca 1", "loca 2", "loca 3")), // 예시 위치 데이터
                  new HashSet<>(Arrays.asList("part 1", "part 2", "part 3")), // 예시 부문 데이터
                  new HashSet<>(Arrays.asList("lang 1", "lang 2", "lang 3")), // 예시 언어 데이터
                  new HashSet<>(Arrays.asList("Photoshop", "Illustrator", "MySQL", "MariaDB")), // 예시 프로그램 데이터
                  bm // 생성된 Mate 객체와 연관
          );
          repoTM.save(tag);

          bm = Mate.builder().title("협업 툴 프로젝트 함께 빌딩해요")
                  .content(".")
                  .author(repoA.findById(5L).orElse(null)).create_date(LocalDateTime.now()).update_date(LocalDateTime.now()).hits(0L).build();
          repoBM.save(bm);
          tag = new Tag(
                  true, // 모집 상태를 기본값으로 설정 (예: true)
                  (i % 2 == 0) ? "PROJECT" : "STUDY", // 목표(goal)을 PROJECT와 STUDY로 번갈아 설정
                  (i % 2 == 0) ? "ONLINE" : "OFFLINE", // 공간(space)을 ONLINE과 OFFLINE으로 번갈아 설정
                  new HashSet<>(Arrays.asList("loca 1", "loca 2", "loca 3")), // 예시 위치 데이터
                  new HashSet<>(Arrays.asList("part 1", "part 2", "part 3")), // 예시 부문 데이터
                  new HashSet<>(Arrays.asList("lang 1", "lang 2", "lang 3")), // 예시 언어 데이터
                  new HashSet<>(Arrays.asList("Photoshop", "Illustrator", "MySQL", "MariaDB")), // 예시 프로그램 데이터
                  bm // 생성된 Mate 객체와 연관
          );

          repoTM.save(tag);
          bm = Mate.builder().title("카카오톡 클론 코딩 온라인 스터디 모집합니다.")
                  .content("카카오톡 클론 코딩 온라인 스터디 모집합니다. 함께 해요")
                  .author(repoA.findById(6L).orElse(null)).create_date(LocalDateTime.now()).update_date(LocalDateTime.now()).hits(0L).build();
          repoBM.save(bm);
          tag = new Tag(
                  true, // 모집 상태를 기본값으로 설정 (예: true)
                  (i % 2 == 0) ? "PROJECT" : "STUDY", // 목표(goal)을 PROJECT와 STUDY로 번갈아 설정
                  (i % 2 == 0) ? "ONLINE" : "OFFLINE", // 공간(space)을 ONLINE과 OFFLINE으로 번갈아 설정
                  new HashSet<>(Arrays.asList("loca 1", "loca 2", "loca 3")), // 예시 위치 데이터
                  new HashSet<>(Arrays.asList("part 1", "part 2", "part 3")), // 예시 부문 데이터
                  new HashSet<>(Arrays.asList("lang 1", "lang 2", "lang 3")), // 예시 언어 데이터
                  new HashSet<>(Arrays.asList("Photoshop", "Illustrator", "MySQL", "MariaDB")), // 예시 프로그램 데이터
                  bm // 생성된 Mate 객체와 연관
          );
          repoTM.save(tag);
        }
        for (Long i = l; i < 3; i++) {
          CommentMate cm1, cm2, cm3;
          cm1 = new CommentMate(null,
                  "1대1 채팅 확인 부탁드려요.",
                  LocalDateTime.now(), LocalDateTime.now(),
                  repoBM.findById(l).orElse(null),
                  repoA.findById(1L).orElse(null), null);
          cm2 = new CommentMate(null,
                  "기간은 언제부터인가요?",
                  LocalDateTime.now(), LocalDateTime.now(),
                  repoBM.findById(l).orElse(null),
                  repoA.findById(2L).orElse(null), null);
          cm3 = new CommentMate(null,
                  "지역은 어디쪽이 좋으세요?",
                  LocalDateTime.now(), LocalDateTime.now(),
                  repoBM.findById(l).orElse(null),
                  repoA.findById(3L).orElse(null), null);
          repoCM.save(cm1);
          repoCM.save(cm2);
          repoCM.save(cm3);
        }
      }

      // 투데이
      Today today = repoBT.findById(l).orElse(null);
      if (today == null) {
        for (Long i = l; i < 4; i++) {
          Today bm;
          bm = new Today(null, "static의 오해와 진실(feat. k6, VisualVM)", "static를 쓰면 메모리를 차지하고, 성능이 저하된다는 이야기를 들었다. 정말 그런지, 왜 그런건지 k6와 VisualVM을 이용한 테스트를 통해 분석해는 과정을 정리했다.",
                  AnswerStatus.NOT_APPLICABLE, 0, 0, 0, null, 0, null, repoA.findById(1L).orElse(null), null, null, "1.png");
          repoBT.save(bm);
          bm = new Today(null, "자바스크립트를 이용한 재시도 로직 구현하기", "이번에 소개해 드릴 글은 다양한 방식의 재시도 전략에 대해서 알려드리고자 합니다. 저자는 자바스크립트에서 활용할 수 있는 6가지 다양한 방식의 전략과 효과적인 수행을 위한 2가지의 라이브러...",
                  AnswerStatus.NOT_APPLICABLE, 0, 0, 0, null, 0, null, repoA.findById(2L).orElse(null), null, null, "2.png");
          repoBT.save(bm);
          bm = new Today(null, "Next.js 버그 픽스로 기여한 후기", "Next.js Contributor가 된 썰 풉니다. \n" + "현재 회사에서 next/script를 사용하여 패키지를 제공하고 있는데요.\n" + "next/script 와 동일하게 strategy에 값을 넣지 않은 경우 default 동작인 afterInteractive를 넣어주고 값이 있는 경우 그 값(afterInteractive, beforeInteractive, worker)로 동작하도록 하고 있습니다.",
                  AnswerStatus.NOT_APPLICABLE, 0, 0, 0, null, 0, null, repoA.findById(3L).orElse(null), null, null, "3.png");
          repoBT.save(bm);
          bm = new Today(null, "성장하는 사람들을 위한 블로그 서비스, üntil", "성장하는 사람들을 위한 블로그 서비스.\n" + "본인만의 간지나는 기술블로그를 만들고 싶다면 한번 사용해보세요.\n" + "곧 뜰거라 레어닉네임 선점해가시면 됩니다.",
                  AnswerStatus.IN_PROGRESS, 0, 0, 0, null, 0, null, repoA.findById(4L).orElse(null), null, null, "4.png");
          repoBT.save(bm);
          bm = new Today(null, "CS50 - 4. 알고리즘 6) 정렬 알고리즘의 실행시간", "버블정렬을 교환이 하나도 일어나지 않을 때로 줄이면 그 시간은 획기적으로 줄어든다.\n" + "Repeat until no swaps\n" + "For i from 0 to n–2\n" + "If i'th and i+1'th elements out of order\n" + "Swap them\n" + "따라서 최종적으로 버블 정렬의 하한은 Ω(n)이된다.",
                  AnswerStatus.IN_PROGRESS, 0, 0, 0, null, 0, null, repoA.findById(5L).orElse(null), null, null, "5.png");
          repoBT.save(bm);
        }
        for (Long i = l; i <= 3L; i++) {
          CommentToday ct = new CommentToday(null, "멋져요!", repoA.findById(i).orElse(null), repoBT.findById(1L).orElse(null), 0, null);
          repoCT.save(ct);
        }
      }

    } catch (Exception e) {
      System.err.println(e);
    }
  }

}