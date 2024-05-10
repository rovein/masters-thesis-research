package ua.nure.sagaresearch.products.event;

import io.eventuate.javaclient.spring.events.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.products.event.config.ProductServiceSourcingConfiguration;
import ua.nure.sagaresearch.products.event.config.ProductsWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@Import({ProductsWebConfiguration.class, ProductServiceSourcingConfiguration.class,
        EventuateDriverConfiguration.class})
public class EventSourcingProductServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(EventSourcingProductServiceMain.class, args);
    }
}
