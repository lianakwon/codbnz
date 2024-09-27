package we.cod.bnz.today.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.today.common.exception.AccountNotFoundException;
import we.cod.bnz.today.common.exception.CommentNotFoundException;
import we.cod.bnz.today.common.exception.ResourceNotFoundException;
import we.cod.bnz.today.common.exception.UnauthorizedException;
import we.cod.bnz.today.dto.request.comment.CommentTodayDTO;
import we.cod.bnz.today.dto.response.comment.ResCommentTodayDTO;
import we.cod.bnz.today.entity.CommentToday;
import we.cod.bnz.today.entity.Today;
import we.cod.bnz.today.repository.CommentTodayRepository;
import we.cod.bnz.today.repository.TodayRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentTodayRepository commentTodayRepository;
    private final TodayRepository todayRepository;
    private final AccountRepository accountRepository;
    public String getUserProfilePicture;

    public Page<ResCommentTodayDTO> getAllComments(Pageable pageable, Long todayId) {
        Page<CommentToday> comments = commentTodayRepository.findAllWithAccountAndToday(pageable, todayId);
        List<ResCommentTodayDTO> commentList = comments.getContent().stream()
                .map(comment -> {
                    String profilePictureFileName = getUserProfilePicture(comment.getAccount());
                    return ResCommentTodayDTO.fromEntity(comment, profilePictureFileName);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }

    public ResCommentTodayDTO write(Long todayId, UserPrincipal account, CommentTodayDTO writeDto) {
        Account commentAccount = getAccountUsername(account);
        // 현재 사용자가 로그인한 상태인지 확인
        if (account == null) {
            throw new UnauthorizedException("로그인한 사용자만 댓글을 작성할 수 있습니다.");
        }
        // board 정보 검색
        Today today = todayRepository.findById(todayId).orElseThrow(
                () -> new ResourceNotFoundException("Today", "Today id", String.valueOf(todayId))
        );
        // Entity 변환 및 댓글 작성자 및 게시글 설정
        CommentToday saveCommentToday = CommentToday.builder()
                .content(writeDto.getContent())
                .account(commentAccount) // 댓글 작성자 설정
                .today(today) // 게시글 설정
                .build();
        CommentToday saveComment = commentTodayRepository.save(saveCommentToday);
        String profilePictureFileName = getUserProfilePicture(commentAccount);
        return ResCommentTodayDTO.fromEntity(saveCommentToday, profilePictureFileName);
    }

    public ResCommentTodayDTO update(Long commentId, CommentTodayDTO commentTodayDto) {
        CommentToday commentToday = commentTodayRepository.findByIdWithAccountAndToday(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );
        commentToday.update(commentTodayDto.getContent());
        String profilePictureFileName = getUserProfilePicture(commentToday.getAccount());
        return ResCommentTodayDTO.fromEntity(commentToday, profilePictureFileName);
    }

    // delete 메서드 수정
    public void delete(Long commentId, UserPrincipal currentUser) {
        CommentToday commentToday = commentTodayRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        // 현재 사용자의 ID
        Long currentUserId = Long.parseLong(currentUser.getUsername());

        // 댓글을 작성한 사용자의 ID
        Long commentAuthorId = commentToday.getAccount().getId();

        // 삭제 권한 확인
        if (!hasPermission(commentAuthorId, currentUserId)) {
            throw new UnauthorizedException("댓글을 삭제할 권한이 없습니다.");
        }

        // 삭제
        commentTodayRepository.deleteById(commentId);
    }

    // 좋아요 추가
    public String addLikeToComment(Long commentId, UserPrincipal accountId) {
        CommentToday commentToday = commentTodayRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
        Account commentAccount = accountRepository.findById(Long.parseLong(accountId.getUsername()))
                .orElseThrow(() -> new AccountNotFoundException("사용자를 찾을 수 없습니다."));

        if (commentToday.getLikedAccounts().contains(commentAccount)) {
            // 이미 추천한 사용자인 경우
            commentToday.removeLike(commentAccount);
            commentTodayRepository.save(commentToday);
            return "추천을 취소할게요";
        }
        commentToday.addLike(commentAccount);
        commentTodayRepository.save(commentToday);
        return "댓글을 추천할게요";
    }
    // 로그인된 사용자를 가져오는 메서드
    private Account getAccountUsername(UserPrincipal username) {
        String account = username.getUsername();
        return accountRepository.findById(Long.parseLong(account))
                .orElseThrow(() -> new IllegalArgumentException("Account username" + username));
    }
    // 게시글 작성자 ID 가져오는 메서드
    public Long getAuthorIdByTodayId(Long todayId) {
        Today today = todayRepository.findById(todayId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + todayId));
        return today.getAccount().getId();
    }

    // 권한 확인 메서드
    public boolean hasPermission(Long targetUserId, Long requestingUserId) {
        return targetUserId.equals(requestingUserId);
    }
    //프로필사진이름
    public String getUserProfilePicture(Account account) {
        if (account == null) {
            // 계정 정보가 없을 경우 기본값 반환 또는 예외 처리
            return "default_profile_picture.png";
        }

        String shape = account.getShape();
        String color = account.getColor();
        String eye = account.getEye();
        String face = account.getFace();

        // 프로필 사진 파일명 생성 (공백 없이 연속된 문자열로 조합)
        return shape + color + eye + face + ".png";
    }
}
