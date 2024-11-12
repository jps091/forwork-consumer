package forwork.forwork_consumer.api.domain.maillog.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailLogJpaRepository extends JpaRepository<MailLogEntity, Long> {
}
