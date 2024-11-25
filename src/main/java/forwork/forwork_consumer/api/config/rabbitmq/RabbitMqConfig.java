package forwork.forwork_consumer.api.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import forwork.forwork_consumer.api.errorhandler.MyErrorHandler;
import forwork.forwork_consumer.api.exception.MyFatalExceptionStrategy;
import forwork.forwork_consumer.api.service.MailLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final ObjectMapper objectMapper;
    private final MailLogService mailLogService;
    private final MyFatalExceptionStrategy myFatalExceptionStrategy;

    @Bean
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new MyErrorHandler(myFatalExceptionStrategy, objectMapper, mailLogService);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}