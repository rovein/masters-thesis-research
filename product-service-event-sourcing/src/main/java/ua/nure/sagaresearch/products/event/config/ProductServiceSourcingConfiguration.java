package ua.nure.sagaresearch.products.event.config;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.products.event.domain.Product;
import ua.nure.sagaresearch.products.event.domain.ProductCommand;
import ua.nure.sagaresearch.products.event.service.ProductWorkflow;

@Configuration
public class ProductServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Product, ProductCommand> sourcingProductRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Product.class, eventStore);
    }

    @Bean
    public ProductWorkflow productWorkflow() {
        return new ProductWorkflow();
    }
}
