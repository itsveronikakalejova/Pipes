package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.pipes.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pipes.consoleui.Menu;
import sk.tuke.gamestudio.game.pipes.core.Field;
import sk.tuke.gamestudio.service.*;

@EnableTransactionManagement
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))

@SpringBootApplication

@Configuration
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);

    }
    @Bean
    CommandLineRunner runner() {
        return s -> new Menu(scoreService(), commentService(), ratingService()).printMenu();
    }

    @Bean
    public Menu menu() {
        return new Menu(scoreService(), commentService(), ratingService());
    }

    @Bean
    public Field field() {
        return new Field(1);
    }

    @Bean
    public ConsoleUI consoleUI(Field field) {
        return new ConsoleUI(field, scoreService(), menu());
    }


    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

