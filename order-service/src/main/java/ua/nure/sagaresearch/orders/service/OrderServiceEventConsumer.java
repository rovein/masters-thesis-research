package ua.nure.sagaresearch.orders.service;

import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import nure.ua.sagaresearch.products.domain.events.ProductQuantityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ua.nure.sagaresearch.baskets.domain.events.BasketCheckedOutEvent;

import java.util.Arrays;

public class OrderServiceEventConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("ua.nure.sagaresearch.baskets.domain.Basket")
                    .onEvent(BasketCheckedOutEvent.class, this::handleBasketCheckedOutEvent)
                .andForAggregateType("ua.nure.sagaresearch.products.domain.Product")
                    .onEvent(ProductQuantityUpdatedEvent.class, this::handleProductQuantityUpdatedEvent)
                .build();
    }

    private void handleBasketCheckedOutEvent(DomainEventEnvelope<BasketCheckedOutEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = Long.parseLong(domainEventEnvelope.getAggregateId());
        log(logger, "{} Received {} for basket {} and order {}",
                PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, event.getOrderId());
        orderService.placeOrder(event.getOrderId(), basketId, event.getTotalPrice(), event.getProductEntries());
    }

    private void handleProductQuantityUpdatedEvent(DomainEventEnvelope<ProductQuantityUpdatedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        var orderId = event.getOrderId();
        var productIds = Arrays.toString(domainEventEnvelope.getAggregateId().split(","));
        log(logger, "{} Received {} for order {} and product IDs: {}",
                CONFIRM_PAYMENT_PREFIX, event.getClass().getSimpleName(), orderId, productIds);
        orderService.approveOrder(orderId);
    }
}
