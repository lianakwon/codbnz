package we.cod.bnz.today.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import we.cod.bnz.today.entity.Thumbnail;
@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {

}
