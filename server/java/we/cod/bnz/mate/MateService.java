package we.cod.bnz.mate;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.mate.common.MateCategory;
import we.cod.bnz.mate.common.MateType;
import we.cod.bnz.mate.dto.*;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.mate.entity.Tag;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.mate.repository.CommentMateRepository;
import we.cod.bnz.mate.repository.MateRepository;
import we.cod.bnz.mate.repository.TagRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MateService {

  private final MateRepository mateRepo;
  private final AccountRepository accountRepo;
  private final TagRepository tagRepo;
  private final CommentMateRepository cMateRepo;

  // 제목 & 내용 검색
  public List<Mate> searchMates(String keyword) {
    return mateRepo.searchMatesByKeyword(keyword);
  }

  @Transactional
  public MateDTO increaseHits(Long mateId) {
    Mate mate = mateRepo.findById(mateId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid mate ID: " + mateId));
    mate.setHits(mate.getHits() + 1);
    mateRepo.save(mate);
    return mate.toDTO();
  }

  // 게시글 전체조회
  public List<ResponseMate> getAllMates() {
    List<Mate> mates = mateRepo.findAll();
    return mates.stream()
            .map(ResponseMate::new)
            .collect(Collectors.toList());
  }

  // 게시글 상세조회
  public ResponseMate getMateDetail(Long mateId) {
    Mate mate = mateRepo.findById(mateId)
            .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다!"));
    return new ResponseMate(mate);
  }

  // 게시글 생성
// mate 엔티티 반환 >
// >>mateDTO를 받아 Mate에게 객체 생성 저장
  public ResponseMate createMate(MateListDTO mateListDTO, UserPrincipal user) {
    Account author = getAccountUsername(user);

    MateType type = getMateTypeFromSpace(mateListDTO.getTag().getSpace());
    MateCategory category = getMateCategoryFromGoal(mateListDTO.getTag().getGoal());

    Mate mate = new Mate(
            mateListDTO.getTitle(),
            mateListDTO.getContent(),
            author,
            mateListDTO.getCreate_date() != null ? mateListDTO.getCreate_date() : LocalDateTime.now(),
            mateListDTO.getUpdate_date() != null ? mateListDTO.getUpdate_date() : LocalDateTime.now(),
            0L,
            type,
            category
    );

    TagDTO tagDTO = mateListDTO.getTag();
    Tag tag = new Tag(
            tagDTO.isRecruit(),
            tagDTO.getGoal(),
            tagDTO.getSpace(),
            tagDTO.getLocas(),
            tagDTO.getParts(),
            tagDTO.getLangs(),
            tagDTO.getPrograms(),
            mate
    );

    mate.setTag(tag);
    mate = mateRepo.save(mate);
    tagRepo.save(tag);

    return new ResponseMate(mate);
  }




  // 게시글 수정
  public ResponseMate updateMate(Long id, MateDTO mateDTO) {
    Mate mate = mateRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid mate ID"));

    mate.updateMate(mateDTO);
    mate = mateRepo.save(mate);

    return new ResponseMate(mate);
  }

  // 게시글 삭제
  public boolean deleteMate(Long mateId, String username) {
    Mate mate = mateRepo.findById(mateId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Mate ID: " + mateId));

    if (!mate.getAuthor().getUsername().equals(username)) {
      throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
    }

    mateRepo.delete(mate);
    return true;
  }

  // 게시글 좋아요
  public void likeMate(Long mateId, String username) {
    Mate mate = mateRepo.findById(mateId)
            .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다!"));
    Account account = accountRepo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다!"));

    if (mate.getLikedAccounts().contains(account)) {
      mate.unlike(account);
    } else {
      mate.like(account);
    }

    mateRepo.save(mate);
  }

  // 댓글 조회
  public List<ResponseComment> getAllCommentsMate(Long mateId) {
    return cMateRepo.findByMateId(mateId).stream()
            .map(ResponseComment::new)
            .collect(Collectors.toList());
  }

  // 댓글 생성
  public CommentMateDTO createCommentMate(Long mateId, String content, String username) {
    Mate mate = mateRepo.findById(mateId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Mate ID: " + mateId));

    Account account = accountRepo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Account: " + username));

    CommentMate comment = new CommentMate();
    comment.setContent(content);
    comment.setMate(mate);
    comment.setWriter(account);
    comment.setCreate_date(LocalDateTime.now());
    comment.setUpdate_date(LocalDateTime.now());

    CommentMate savedComment = cMateRepo.save(comment);
    return new CommentMateDTO(savedComment);
  }

  // 댓글 수정
  public ResponseComment updateCommentMate(Long commentId, String content, String username) {
    CommentMate comment = cMateRepo.findById(commentId).orElseThrow();
    if (!comment.getWriter().getUsername().equals(username)) {
      throw new IllegalArgumentException("Only the author can update the comment");
    }
    comment.setContent(content);
    comment.setUpdate_date(LocalDateTime.now());
    cMateRepo.save(comment);
    return new ResponseComment(comment);
  }

  // 댓글 삭제
  public void deleteCommentMate(Long commentId, String username) {
    CommentMate comment = cMateRepo.findById(commentId).orElseThrow();
    if (!comment.getWriter().getUsername().equals(username)) {
      throw new IllegalArgumentException("Only the author can delete the comment");
    }
    cMateRepo.delete(comment);
  }

  // 댓글 좋아요
  public ResponseComment likeComment(Long commentId, String username) {
    Account account = accountRepo.findByUsername(username).orElseThrow();
    CommentMate comment = cMateRepo.findById(commentId).orElseThrow();
    comment.like(account);
    cMateRepo.save(comment);
    return new ResponseComment(comment);
  }

  public ResponseComment unlikeComment(Long commentId, String username) {
    Account account = accountRepo.findByUsername(username).orElseThrow();
    CommentMate comment = cMateRepo.findById(commentId).orElseThrow();
    comment.unlike(account);
    cMateRepo.save(comment);
    return new ResponseComment(comment);
  }

  // 로그인된 사용자를 가져오는 메서드
  private Account getAccountUsername(UserPrincipal username) {
    System.out.println("username = " + username);
    String account = username.getUsername();
    return accountRepo.findById(Long.parseLong(account))
            .orElseThrow(() -> new IllegalArgumentException("Account username" + username + "not"));
  }
  // MateType과 MateCategory를 설정하는 메서드들 추가
  private MateType getMateTypeFromSpace(String space) {
    return space.equals("ONLINE") ? MateType.ONLINE : MateType.OFFLINE;
  }

  private MateCategory getMateCategoryFromGoal(String goal) {
    return goal.equals("PROJECT") ? MateCategory.PROJECT : MateCategory.STUDY;
  }
}
