package we.cod.bnz.today.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.generic.RET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import we.cod.bnz.account.Account;
import we.cod.bnz.account.oauth.UserPrincipal;
import we.cod.bnz.account.repository.AccountRepository;
import we.cod.bnz.today.common.exception.AccountNotFoundException;
import we.cod.bnz.today.common.exception.BoardNotFoundException;
import we.cod.bnz.today.common.exception.ResourceNotFoundException;
import we.cod.bnz.today.common.exception.UnauthorizedException;
import we.cod.bnz.today.dto.request.board.SearchData;
import we.cod.bnz.today.dto.request.board.TodayUpdateDTO;
import we.cod.bnz.today.dto.request.board.TodayWriteDTO;
import we.cod.bnz.today.dto.response.board.ResTodayDetailsDTO;
import we.cod.bnz.today.dto.response.board.ResTodayListDTO;
import we.cod.bnz.today.dto.response.board.ResTodayWriteDTO;
import we.cod.bnz.today.entity.Today;
import we.cod.bnz.today.repository.TodayRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodayService {

    private static final Logger logger = LoggerFactory.getLogger(TodayService.class);

    private final TodayRepository todayRepository;
    private final AccountRepository accountRepository;
    private String profilePictureFileName;

    // 페이징 리스트
    public Page<ResTodayListDTO> getAllToday(Pageable pageable) {
        Page<Today> today = todayRepository.findAllWithAccountAndComments(pageable);
        List<ResTodayListDTO> list = today.getContent().stream()
                .map(ResTodayListDTO::fromEntity)
                .collect(Collectors.toList());
        ResTodayListDTO dto = new ResTodayListDTO();
        return new PageImpl<>(list, pageable, today.getTotalElements());
    }

    // 게시글 검색, isEmpty() == ""
    public Page<ResTodayListDTO> search(SearchData searchData, Pageable pageable) {
        Page<Today> result = null;
        if (!searchData.getTitle().isEmpty()) {
            result = todayRepository.findAllTitleContaining(searchData.getTitle(), pageable);
        } else if (!searchData.getContent().isEmpty()) {
            result = todayRepository.findAllContentContaining(searchData.getContent(), pageable);
        } else if (!searchData.getWriterName().isEmpty()) {
            result = todayRepository.findAllUsernameContaining(searchData.getWriterName(), pageable);
        }
        List<ResTodayListDTO> list = result.getContent().stream()
                .map(ResTodayListDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    // 게시글 등록
    public ResTodayWriteDTO write(TodayWriteDTO todayWriteDto, UserPrincipal account) {
        Account todayAccount = getAccountUsername(account);
        Today today = Today.builder()
                .title(todayWriteDto.getTitle())
                .content(todayWriteDto.getContent())
                .account(todayAccount)
                .answerStatus(todayWriteDto.getAnswerStatus())
                .build();

        Today saveToday = todayRepository.save(today);
        return ResTodayWriteDTO.fromEntity(saveToday, todayAccount);
    }

    // 게시글 상세보기
    public ResTodayDetailsDTO detail(Long todayId) {
        Today findToday = todayRepository.findByIdWithAccountAndCommentsAndFiles(todayId).orElseThrow(
               () -> new ResourceNotFoundException("Today", "Today Id", String.valueOf(todayId))
       );
       // 조회수 증가
       findToday.upViewCount();
       // 사용자의 프로필 사진 파일 이름 가져오기
       String profilePictureFileName = getUserProfilePicture(findToday.getAccount());
       return ResTodayDetailsDTO.fromEntity(findToday, profilePictureFileName);
    }

    // 게시글 수정
    public ResTodayDetailsDTO update(Long todayId, TodayUpdateDTO todayUpdateDto, Long userId) {
        // 게시글 정보 가져오기
        Today updateToday = todayRepository.findByIdWithAccountAndCommentsAndFiles(todayId).orElseThrow(
                () -> new ResourceNotFoundException("Today", "Today Id", String.valueOf(todayId))
        );
        // 게시글 작성자 ID 가져오기
        Long authorId = updateToday.getAccount().getId();
        // 권한 확인
        if (!hasPermission(authorId, userId)) {
            throw new UnauthorizedException("해당 게시글을 수정할 권한이 없습니다.");
        }
        // 게시글 업데이트
        updateToday.update(todayUpdateDto.getTitle(), todayUpdateDto.getContent(),todayUpdateDto.getAnswerStatus());
        return ResTodayDetailsDTO.fromEntity(updateToday, profilePictureFileName);
    }

    // 게시글 삭제
    public void delete(Long todayId, Long userId) {
        Today deleteToday = todayRepository.findByIdWithAccountAndCommentsAndFiles(todayId).orElseThrow(
                () -> new ResourceNotFoundException("Today", "Today Id", String.valueOf(todayId))
        );
        Long authorId = deleteToday.getAccount().getId();
        // 권한 확인
        if (!hasPermission(authorId, userId)) {
            throw new UnauthorizedException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        todayRepository.deleteById(todayId);
    }

    // 좋아요 추가
    public String addLikeToToday(Long todayId, String memberId) {
        Today today = todayRepository.findById(todayId)
                .orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));
        Account account = (Account) accountRepository.findAccountByUsername(memberId)
                .orElseThrow(() -> new AccountNotFoundException("사용자를 찾을 수 없습니다."));

        if (today.getLikedAccounts().contains(account)) {
            // 이미 추천한 사용자인 경우
            today.removeLike(account);
            todayRepository.save(today);
            return "추천을 취소할게요.";
        }
        today.addLike(account);
        todayRepository.save(today);
        return "게시글을 추천할게요!.";
    }

    // 궁금해요 추가
    public String addQuestionToToday(Long todayId, String memberId) {
        Today today = todayRepository.findById(todayId)
                .orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다."));
        Account account = (Account) accountRepository.findAccountByUsername(memberId)
                .orElseThrow(() -> new AccountNotFoundException("사용자를 찾을 수 없습니다."));

        if (today.getQuestionAccounts().contains(account)) {
            // 이미 추천한 사용자인 경우
            today.removeQuestion(account);
            todayRepository.save(today);
            return "궁금하지 않아요";
        }
        today.addQuestion(account);
        todayRepository.save(today);
        return "저도 궁금해요";
    }
    // 보드의 댓글 수 갱신 메서드
    public void updateCommentCount(Long todayId) {
        Today today = todayRepository.findById(todayId)
                .orElseThrow(() -> new BoardNotFoundException("게시글을 찾을 수 없습니다. ID: " + todayId));
        int commentCount = today.getComments().size();
        today.setCommentCount(commentCount);
        todayRepository.save(today);
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
                .orElseThrow(() -> new EntityNotFoundException("Today not found with id: " + todayId));
        return today.getAccount().getId();
    }

    // 권한 확인 메서드
    public boolean hasPermission(Long targetUserId, Long requestingUserId) {
        return targetUserId.equals(requestingUserId);
    }
    //프로필사진이름
    private String getUserProfilePicture(Account account) {
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
