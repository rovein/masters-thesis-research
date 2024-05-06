package ua.nure.sagaresearch.orders;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.orders.web.OrderWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@Import({OrderWebConfiguration.class, OrderConfiguration.class, OrderServiceSourcingConfiguration.class,
        EventuateDriverConfiguration.class})
public class OrderServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceMain.class, args);
    }
}
