package es.uca;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "cursoia")
@Push
@EnableAsync
public class Application implements AppShellConfigurator, CommandLineRunner {

    public static String PROFILES;
    @Autowired
    public Environment environment;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void init() {
        PROFILES = String.join(", ", environment.getActiveProfiles());
    }

    @Bean
    public EmbeddingModel getEmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }


    @Override
    public void run(String... args) throws Exception {

        logger.info("CURSO AI is now available... " + Arrays.stream(environment.getActiveProfiles()).toList());

    }

}
