package ua.nure.sagaresearch.products;

import io.eventuate.javaclient.spring.events.EnableEventHandlers;
import io.eventuate.local.java.spring.javaclient.driver.EventuateDriverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.products.config.ProductServiceConfiguration;
import ua.nure.sagaresearch.products.config.ProductServiceSourcingConfiguration;
import ua.nure.sagaresearch.products.config.ProductsWebConfiguration;

@SpringBootApplication
@EnableEventHandlers
@Import({ProductsWebConfiguration.class, ProductServiceConfiguration.class, ProductServiceSourcingConfiguration.class,
        EventuateDriverConfiguration.class})
public class ProductServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceMain.class, args);
    }
}
