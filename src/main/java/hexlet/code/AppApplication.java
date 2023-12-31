package hexlet.code;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.sentry.Sentry;


@EnableJpaAuditing
@SpringBootApplication

@OpenAPIDefinition(
        info = @Info(
                title = "Users API",
                version = "1.0",
                description = "Information about users"
        )
)
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
    }
    @Bean
    public Faker getFaker() {
        return new Faker();
    }

}
