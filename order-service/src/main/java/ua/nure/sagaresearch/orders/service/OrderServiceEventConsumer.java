package ua.nure.sagaresearch.orders.service;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.nure.sagaresearch.baskets.domain.events.BasketCheckedOutEvent;
import ua.nure.sagaresearch.baskets.domain.events.customer.CustomerCreditReservationFailedEvent;
import ua.nure.sagaresearch.baskets.domain.events.customer.CustomerCreditReservedEvent;
import ua.nure.sagaresearch.baskets.domain.events.customer.CustomerValidationFailedEvent;
import ua.nure.sagaresearch.common.domain.LoggingUtils;

public class OrderServiceEventConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("ua.nure.sagaresearch.baskets.domain.Basket")
                    .onEvent(BasketCheckedOutEvent.class, this::handleBasketCheckedOutEvent)
                .build();
    }

    private void handleBasketCheckedOutEvent(DomainEventEnvelope<BasketCheckedOutEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = Long.parseLong(domainEventEnvelope.getAggregateId());
        logger.info("{} Received {} for basket {} and order {}",
                LoggingUtils.PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, event.getOrderId());
        orderService.placeOrder(event.getOrderId(), basketId, event.getTotalPrice(), event.getProductEntries());
    }
}
