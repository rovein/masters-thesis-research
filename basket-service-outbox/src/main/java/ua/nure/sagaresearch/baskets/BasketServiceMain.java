package ua.nure.sagaresearch.baskets;

import io.eventuate.javaclient.spring.events.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.nure.sagaresearch.baskets.config.BasketServiceConfiguration;
import ua.nure.sagaresearch.baskets.config.BasketServiceSourcingConfiguration;
import ua.nure.sagaresearch.baskets.config.BasketWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@EnableJpaRepositories
@Import({BasketServiceConfiguration.class, BasketWebConfiguration.class, BasketServiceSourcingConfiguration.class,
        EventuateDriverConfiguration.class})
public class BasketServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(BasketServiceMain.class, args);
    }
}
