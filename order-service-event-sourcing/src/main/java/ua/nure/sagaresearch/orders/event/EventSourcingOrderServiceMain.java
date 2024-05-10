package ua.nure.sagaresearch.orders.event;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ua.nure.sagaresearch.orders.event.config.OrderServiceSourcingConfiguration;
import ua.nure.sagaresearch.orders.event.config.OrderWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@EnableJpaRepositories
@Import({OrderWebConfiguration.class, OrderServiceSourcingConfiguration.class, EventuateDriverConfiguration.class})
public class EventSourcingOrderServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(EventSourcingOrderServiceMain.class, args);
    }
}
