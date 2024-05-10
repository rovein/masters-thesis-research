package ua.nure.sagaresearch.orders.event.config;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.orders.event.domain.Order;
import ua.nure.sagaresearch.orders.event.domain.OrderCommand;

@Configuration
public class OrderServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Order, OrderCommand> sourcingOrderRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Order.class, eventStore);
    }
}
