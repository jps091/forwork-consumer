package forwork.forwork_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForworkConsumerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(ForworkConsumerApplication.class)
				.profiles("prod")
				.run(args);
	}
}
