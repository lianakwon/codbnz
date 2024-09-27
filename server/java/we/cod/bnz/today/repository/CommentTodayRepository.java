package we.cod.bnz.today.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import we.cod.bnz.today.entity.CommentToday;
import we.cod.bnz.today.entity.Today;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentTodayRepository extends JpaRepository<CommentToday, Long> {

    @Query(value = "SELECT c FROM CommentToday c JOIN FETCH c.account JOIN FETCH c.today b WHERE b.id = :todayId")
    Page<CommentToday> findAllWithAccountAndToday(Pageable pageable, Long todayId);

    @Query(value = "SELECT c FROM CommentToday c JOIN FETCH c.account m JOIN FETCH c.today b WHERE c.id = :commentId")
    Optional<CommentToday> findByIdWithAccountAndToday(Long commentId);

    List<CommentToday> findByToday(Today today);

    // 작성댓글
    @Query(value = " SELECT *     FROM CommentToday " +
            " WHERE account_id = :account_id ", nativeQuery = true)
    List<CommentToday> get_all_comments_today(@Param("account_id") Long today_id);

    // 작성댓글
    @Query(value = " SELECT C.*     FROM today T " +
            " JOIN  CommentToday C  ON T.today_id = C.today_id " +
            " JOIN  account A       ON A.id = C.account_id " +
            " WHERE C.account_id = :id ", nativeQuery = true)
    List<CommentToday> get_comments_today(@Param("id") Long account_id);

    // 좋아요
    @Query(value = " SELECT C.*     FROM today T " +
            " JOIN  CommentToday C  ON T.today_id = C.today_id " +
            " JOIN  account A       ON A.id = C.account_id " +
            " WHERE C.comment_id    IN ( SELECT L.comment_id  FROM comment_likes L " +
            "                           WHERE L.account_id = :id ) ", nativeQuery = true)
    List<CommentToday> get_likes_comments_today(@Param("id") Long account_id);


}
