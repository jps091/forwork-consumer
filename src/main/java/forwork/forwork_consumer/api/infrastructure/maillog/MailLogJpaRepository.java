package forwork.forwork_consumer.api.infrastructure.maillog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MailLogJpaRepository extends JpaRepository<MailLogEntity, Long> {
}
