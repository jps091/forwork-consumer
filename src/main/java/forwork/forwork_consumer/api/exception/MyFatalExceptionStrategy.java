package forwork.forwork_consumer.api.exception;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class MyFatalExceptionStrategy implements FatalExceptionStrategy {

    private final FatalExceptionStrategy fatalExceptionStrategy = new ConditionalRejectingErrorHandler.DefaultExceptionStrategy();

    /**
     * 치명적 에러로 간주하고 재처리하지 않는다.
     */
    @Override
    public boolean isFatal(Throwable t) {
        return fatalExceptionStrategy.isFatal(t)
                || t.getCause() instanceof CustomSesException
                || t.getCause() instanceof RedisConnectionFailureException
                || t.getCause() instanceof MailAuthenticationException
                || t.getCause() instanceof InvalidEmailException;
    }
}