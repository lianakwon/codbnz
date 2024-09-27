package we.cod.bnz.mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.mate.entity.CommentMate;
import we.cod.bnz.mate.entity.Mate;

import java.util.List;

public interface CommentMateRepository extends JpaRepository<CommentMate, Long> {

  List<CommentMate> findByMate(Mate mate);

  @Query(value = " SELECT * FROM comments_m " +
          " WHERE account_id = :account_id ", nativeQuery = true)
  List<CommentMate> get_all_comments_mate(@Param("account_id") Long today_id);

  @Query(value = "SELECT * FROM comments_m WHERE mate_id = :mate_id", nativeQuery = true)
  List<CommentMate> findByMateId(@Param("mate_id") Long mateId);

  // =============
  // 작성글 / 작성댓글

  @Query(value = " SELECT C.* "   + " FROM mates M " +
          " JOIN  comments_m C "  + " ON M.id = C.mate_id " +
          " JOIN  account A "     + " ON A.id = C.writer_id " +
          " WHERE C.writer_id = :id ", nativeQuery = true)
  List<CommentMate> get_comments_mate(@Param("id") Long account_id);

  // =============
  // 좋아요 / 궁금해요

  @Query(value = " SELECT C.* "   + " FROM mates M " +
          " JOIN  comments_m C "  + " ON M.id = C.mate_id " +
          " JOIN  account A "     + " ON A.id = C.writer_id " +
          " WHERE M.id IN ( SELECT L.comment_m_id  FROM comment_m_likes L " +
          "                 WHERE  L.account_id = :id ) ", nativeQuery = true)
  List<CommentMate> get_likes_comments_mate(@Param("id") Long account_id);

  boolean existsByWriterIdAndMateId(Long writerId, Long mateId);
 // 댓글이 있는지
}