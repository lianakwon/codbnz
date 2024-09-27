package we.cod.bnz.mate.common;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import we.cod.bnz.mate.entity.Mate;
import we.cod.bnz.mate.repository.MateRepository;

// 조회수 증가 비동기 변경 service
// 메인 스레드 -> 별도 스레드 실행
@Service
public class AsyncService {

    @Autowired
    private MateRepository mateRepo;

    @Async
    public void incrementHitsAsync(Long mateId) {
        Mate mate = mateRepo.findById(mateId)
                .orElseThrow(() -> new EntityNotFoundException("Mate not found"));
        mate.getHits();
        mateRepo.save(mate);
    }
}
