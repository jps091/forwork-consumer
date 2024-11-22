package forwork.forwork_consumer.api.errorhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forwork.forwork_consumer.api.consumer.deadletter.message.FailMessage;
import forwork.forwork_consumer.api.service.MailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.util.ErrorHandler;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
public class MyErrorHandler implements ErrorHandler {

    private final FatalExceptionStrategy exceptionStrategy;
    private final ObjectMapper objectMapper;
    private final MailLogService mailLogService;

    @Override
    public void handleError(Throwable e) {
        if (this.exceptionStrategy.isFatal(e) && e instanceof ListenerExecutionFailedException failedException) {
            Message failedMessage = failedException.getFailedMessage();
            mailLogRegister(failedMessage, e);
            throw new ImmediateAcknowledgeAmqpException(
                    "Fatal exception Immediate Discarding: " + e.getMessage(), e);
        }

        throw new AmqpRejectAndDontRequeueException(
                "Moving to DLX for retries: " + e.getMessage(), e);
    }

    private void mailLogRegister(Message failedMessage, Throwable e) {
        String messageBody = new String(failedMessage.getBody(), StandardCharsets.UTF_8);
        FailMessage message = parsing(messageBody);
        mailLogService.registerFailLog(message, e);
    }

    private FailMessage parsing(String messageBody) {
        try {
            return objectMapper.readValue(messageBody, FailMessage.class);
        } catch (JsonProcessingException e) {
            log.error("Json Parsing ={}", messageBody);
            throw new IllegalStateException("Json Parsing Error");
        }
    }
}