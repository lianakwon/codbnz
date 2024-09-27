package we.cod.bnz.today.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import we.cod.bnz.today.entity.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
