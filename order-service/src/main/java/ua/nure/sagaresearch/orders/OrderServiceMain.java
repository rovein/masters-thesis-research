package ua.nure.sagaresearch.orders;

import io.eventuate.local.java.spring.common.jdbckafkastore.EventuateLocalConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.orders.web.OrderWebConfiguration;

@SpringBootApplication
@Import({OrderWebConfiguration.class, OrderConfiguration.class, OrderServiceSourcingConfiguration.class, EventuateLocalConfiguration.class})
public class OrderServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceMain.class, args);
    }
}
