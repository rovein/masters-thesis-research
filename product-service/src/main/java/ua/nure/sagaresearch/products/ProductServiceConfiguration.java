package ua.nure.sagaresearch.products;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.products.domain.ProductsEventConsumer;
import ua.nure.sagaresearch.products.service.ProductService;

@Configuration
@Import({TramNoopDuplicateMessageDetectorConfiguration.class})
public class ProductServiceConfiguration {

    @Bean
    public ProductsEventConsumer customerHistoryEventConsumer(ProductService productService) {
        return new ProductsEventConsumer(productService);
    }

    @Bean("productsDomainEventDispatcher")
    public DomainEventDispatcher productsDomainEventDispatcher(ProductsEventConsumer productsEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("productServiceEvents", productsEventConsumer.domainEventHandlers());
    }
}
