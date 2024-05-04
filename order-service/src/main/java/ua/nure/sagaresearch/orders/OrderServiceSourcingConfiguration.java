package ua.nure.sagaresearch.orders;

import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.nure.sagaresearch.orders.domain.event.Order;
import ua.nure.sagaresearch.orders.domain.event.OrderCommand;

@Configuration
public class OrderServiceSourcingConfiguration {

    @Bean
    public AggregateRepository<Order, OrderCommand> sourcingOrderRepository(EventuateAggregateStore eventStore) {
        return new AggregateRepository<>(Order.class, eventStore);
    }
}
