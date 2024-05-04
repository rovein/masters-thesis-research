package ua.nure.sagaresearch.products;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.products.domain.event.Product;
import ua.nure.sagaresearch.products.domain.event.ProductCommand;

@Configuration
public class ProductServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Product, ProductCommand> sourcingProductRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Product.class, eventStore);
    }
}
