package ua.nure.sagaresearch.baskets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.baskets.web.BasketWebConfiguration;

@SpringBootApplication
@Import({BasketServiceConfiguration.class, BasketWebConfiguration.class})
public class BasketServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BasketServiceMain.class, args);
    }
}
