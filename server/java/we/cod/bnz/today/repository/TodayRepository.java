package we.cod.bnz.today.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import we.cod.bnz.today.entity.Today;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodayRepository extends JpaRepository<Today, Long> {

  // 게시글 상세 조회, @BatchSize : Comments와 Files는 필요할 때 in 절로 가져옴
  @Query(value = "SELECT b FROM Today b JOIN FETCH b.account WHERE b.id = :todayID")
  Optional<Today> findByIdWithAccountAndCommentsAndFiles(Long todayID);

  // 첫 페이징 화면("/")
  @Query(value = "SELECT b FROM Today b JOIN FETCH b.account")
  Page<Today> findAllWithAccountAndComments(Pageable pageable);

  // 제목 검색
  @Query(value = "SELECT b FROM Today b JOIN FETCH b.account WHERE b.title LIKE %:title%")
  Page<Today> findAllTitleContaining(String title, Pageable pageable);

  // 내용 검색
  @Query(value = "SELECT b FROM Today b JOIN FETCH b.account WHERE b.content LIKE %:content%")
  Page<Today> findAllContentContaining(String content, Pageable pageable);

  // 작성자 검색
  @Query(value = "SELECT b FROM Today b JOIN FETCH b.account WHERE b.account.nickname LIKE %:nickname%")
  Page<Today> findAllUsernameContaining(String nickname, Pageable pageable);


  // 메인
  @Query(value = " SELECT *     FROM today " +
          " ORDER BY today_id DESC    LIMIT 8 ", nativeQuery = true)
  List<Today> main_boards_today();

  // 작성글 / 작성댓글
  @Query(value = " SELECT T.* " + " FROM  today T " +
          " JOIN  account A   " + " ON T.account_id = A.id " +
          " WHERE T.account_id = :id ", nativeQuery = true)
  List<Today> get_boards_today(@Param("id") Long accountId);

  // 좋아요 / 궁금해요
  @Query(value = " SELECT T.* " + " FROM today T " +
          " JOIN  account A   " + " ON A.id = T.account_id " +
          " WHERE T.today_id IN ( SELECT L.today_id  FROM today_likes L " +
          "                 WHERE L.account_id = :id ) ", nativeQuery = true)
  List<Today> get_likes_today(@Param("id") Long account_id);

  @Query(value = " SELECT T.* " + " FROM today T " +
          " JOIN account A    " + " ON A.id = T.account_id " +
          " WHERE T.today_id IN ( SELECT L.today_id  FROM today_likes L " +
          "                 WHERE L.account_id = :id ) ", nativeQuery = true)
  List<Today> get_quests_today(@Param("id") Long account_id);

}
