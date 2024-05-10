package ua.nure.sagaresearch.baskets;

import io.eventuate.javaclient.spring.events.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.baskets.config.BasketServiceConfiguration;
import ua.nure.sagaresearch.baskets.config.BasketWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@Import({BasketServiceConfiguration.class, BasketWebConfiguration.class,
        EventuateDriverConfiguration.class})
public class OutboxBasketServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(OutboxBasketServiceMain.class, args);
    }
}
