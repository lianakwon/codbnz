package we.cod.bnz.mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.cod.bnz.mate.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

  @Override
  Optional<Tag> findById(Long id);

}
