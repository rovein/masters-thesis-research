package ua.nure.sagaresearch.baskets;

import ua.nure.sagaresearch.baskets.web.BasketWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({BasketServiceConfiguration.class, BasketWebConfiguration.class})
public class BasketServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BasketServiceMain.class, args);
    }
}
