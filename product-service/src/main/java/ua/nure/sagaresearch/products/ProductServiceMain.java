package ua.nure.sagaresearch.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.products.web.ProductsWebConfiguration;

@SpringBootApplication
@Import({ProductsWebConfiguration.class, ProductServiceConfiguration.class})
public class ProductServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceMain.class, args);
    }
}
