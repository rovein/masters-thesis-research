package ua.nure.sagaresearch.baskets;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.baskets.domain.event.Basket;
import ua.nure.sagaresearch.baskets.domain.event.BasketCommand;
import ua.nure.sagaresearch.baskets.service.event.BasketWorkflow;

@Configuration
public class BasketServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Basket, BasketCommand> sourcingCustomerRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Basket.class, eventStore);
    }

    @Bean
    public BasketWorkflow basketWorkflow() {
        return new BasketWorkflow();
    }

}
