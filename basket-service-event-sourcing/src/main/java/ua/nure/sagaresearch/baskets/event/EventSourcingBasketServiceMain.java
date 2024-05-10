package ua.nure.sagaresearch.baskets.event;

import io.eventuate.javaclient.spring.events.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.nure.sagaresearch.baskets.event.config.BasketServiceSourcingConfiguration;
import ua.nure.sagaresearch.baskets.event.config.BasketWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@EnableJpaRepositories
@Import({BasketWebConfiguration.class, BasketServiceSourcingConfiguration.class, EventuateDriverConfiguration.class})
public class EventSourcingBasketServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(EventSourcingBasketServiceMain.class, args);
    }
}
