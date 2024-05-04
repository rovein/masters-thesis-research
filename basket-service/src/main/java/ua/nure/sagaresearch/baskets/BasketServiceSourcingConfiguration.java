package ua.nure.sagaresearch.baskets;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.baskets.domain.event.Basket;
import ua.nure.sagaresearch.baskets.domain.event.BasketCommand;

@Configuration
public class BasketServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Basket, BasketCommand> customerRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Basket.class, eventStore);
    }

}
