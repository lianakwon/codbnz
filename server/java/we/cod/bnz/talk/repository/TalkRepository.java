package we.cod.bnz.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import we.cod.bnz.talk.entity.Talk;

import java.util.List;

public interface TalkRepository extends JpaRepository<Talk, Long> {

  @Query(value = "SELECT T.* "       + " FROM talks T " +
          " JOIN account A1 "         + " ON A1.id = T.from_id " +
          " JOIN account A2 "         + " ON A2.id = T.to_id " +
          " WHERE A1.id = :from_id OR A2.id = :from_id " , nativeQuery = true)
  List<Talk> find_by_from(@Param("from_id") Long from_id);
}
