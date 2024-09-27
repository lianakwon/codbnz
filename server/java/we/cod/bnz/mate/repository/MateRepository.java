package we.cod.bnz.mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.mate.entity.Mate;

import java.util.List;

public interface MateRepository extends JpaRepository<Mate, Long> {

  // =============
  // 작성글 / 작성댓글

  @Query(value = "SELECT M.* FROM mates M JOIN account A ON M.author_id = A.id WHERE M.author_id = :account_id", nativeQuery = true)
  List<Mate> get_boards_mate(@Param("account_id") Long accountId);

  // 좋아요 조회 메소드
  @Query(value = "SELECT M.* FROM mates M JOIN account A ON A.id = M.author_id WHERE M.id IN (SELECT mate_id FROM mate_likes WHERE account_id = :account_id)", nativeQuery = true)
  List<Mate> get_likes_mate(@Param("account_id") Long accountId);

  // 검색 메소드
  @Query("SELECT m FROM Mate m WHERE m.title LIKE %:keyword% OR m.content LIKE %:keyword%")
  List<Mate> searchMatesByKeyword(@Param("keyword") String keyword);

  @Query(value = " SELECT *     FROM mates " +
          " ORDER BY id DESC    LIMIT 8 ", nativeQuery = true)
  List<Mate> main_boards_mate();
}
