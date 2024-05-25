package ua.nure.sagaresearch.baskets.config;

import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.viewsupport.rebuild.DBLockService;
import io.eventuate.tram.viewsupport.rebuild.DomainEventWithEntityId;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportServiceFactory;
import io.eventuate.tram.viewsupport.rebuild.SnapshotConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ua.nure.sagaresearch.baskets.domain.Basket;
import ua.nure.sagaresearch.baskets.domain.BasketRepository;
import ua.nure.sagaresearch.baskets.domain.events.BasketSnapshotEvent;
import ua.nure.sagaresearch.baskets.service.BasketServiceEventConsumer;

@Configuration
@Import({SnapshotConfiguration.class})
public class BasketServiceConfiguration {

    @Bean
    public DomainEventDispatcher domainEventDispatcher(BasketServiceEventConsumer basketServiceEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("basketServiceEvents", basketServiceEventConsumer.domainEventHandlers());
    }

    @Bean
    public DomainSnapshotExportService<Basket> domainSnapshotExportService(BasketRepository basketRepository,
                                                                           DomainSnapshotExportServiceFactory<Basket> domainSnapshotExportServiceFactory) {
        return domainSnapshotExportServiceFactory.make(
                Basket.class,
                basketRepository,
                basket -> {
                    DomainEvent domainEvent = new BasketSnapshotEvent(basket.getId(),
                            basket.getTotalQuantity(),
                            basket.getTotalPrice());

                    return new DomainEventWithEntityId(basket.getId(), domainEvent);
                },
                new DBLockService.TableSpec("basket", "basket0_"),
                "MySqlReader");
    }
}
