package forwork.forwork_consumer.api.infrastructure.resume;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeJpaRepository extends JpaRepository<ResumeEntity, Long>{

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ResumeEntity r where r.id = :resumeId")
    Optional<ResumeEntity> findByIdWithPessimisticLock(@Param("resumeId") Long resumeId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select r from ResumeEntity r where r.id = :resumeId")
    Optional<ResumeEntity> findByIdWithOptimisticLock(@Param("resumeId") Long resumeId);
}
