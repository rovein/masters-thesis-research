package ua.nure.sagaresearch.orders.config;

import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import io.eventuate.tram.viewsupport.rebuild.DBLockService;
import io.eventuate.tram.viewsupport.rebuild.DomainEventWithEntityId;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportServiceFactory;
import io.eventuate.tram.viewsupport.rebuild.SnapshotConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.orders.domain.Order;
import ua.nure.sagaresearch.orders.domain.OrderRepository;
import ua.nure.sagaresearch.orders.domain.events.OrderSnapshotEvent;
import ua.nure.sagaresearch.orders.service.OrderService;
import ua.nure.sagaresearch.orders.service.OrderServiceEventConsumer;

@Configuration
@Import({OptimisticLockingDecoratorConfiguration.class, SnapshotConfiguration.class})
public class OrderConfiguration {

    @Bean
    public OrderService orderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
        return new OrderService(domainEventPublisher, orderRepository);
    }

    @Bean
    public OrderServiceEventConsumer orderEventConsumer() {
        return new OrderServiceEventConsumer();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(OrderServiceEventConsumer orderServiceEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("orderServiceEvents", orderServiceEventConsumer.domainEventHandlers());
    }

    @Bean
    public DomainSnapshotExportService<Order> domainSnapshotExportService(OrderRepository orderRepository,
                                                                          DomainSnapshotExportServiceFactory<Order> domainSnapshotExportServiceFactory) {
        return domainSnapshotExportServiceFactory.make(
                Order.class,
                orderRepository,
                order -> {
                    DomainEvent domainEvent = new OrderSnapshotEvent(order.getId(),
                            Long.valueOf(order.getOrderDetails().getBasketId()),
                            order.getTotalPrice(),
                            order.getState());
                    return new DomainEventWithEntityId(order.getId(), domainEvent);
                },
                new DBLockService.TableSpec("orders", "order0_"),
                "MySqlReader");
    }
}
