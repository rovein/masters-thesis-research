package ua.nure.sagaresearch.baskets.service;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidationFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.common.domain.LoggingUtils;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.OrderPlacedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderPlacementRequestedEvent;

@Service("orderEventConsumer")
public class BasketServiceEventConsumer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private BasketService basketService;

    public BasketServiceEventConsumer(BasketService basketService) {
        this.basketService = basketService;
    }

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("ua.nure.sagaresearch.products.domain.Product")
                    .onEvent(ProductBasketAdditionValidatedEvent.class, this::handleProductBasketAdditionValidatedEvent)
                    .onEvent(ProductBasketAdditionValidationFailedEvent.class, this::handleProductBasketAdditionValidationFailedEvent)
                .andForAggregateType("ua.nure.sagaresearch.orders.domain.Order")
                    .onEvent(OrderPlacementRequestedEvent.class, this::handleOrderPlacementRequestedEvent)
                    .onEvent(OrderPlacedEvent.class, this::handleOrderPlacedEvent)
                .build();
    }

    public void handleProductBasketAdditionValidatedEvent(DomainEventEnvelope<ProductBasketAdditionValidatedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        logger.info("%s Received product addition validation event, transaction completed successfully for productId %s and basketId %s"
                .formatted(LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX, domainEventEnvelope.getAggregateId(), event.getBasketId()));
    }

    public void handleProductBasketAdditionValidationFailedEvent(DomainEventEnvelope<ProductBasketAdditionValidationFailedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long productId = Long.parseLong(domainEventEnvelope.getAggregateId());
        Long basketId = event.getBasketId();
        Long quantity = event.getQuantity();
        Money pricePerUnit = event.getPricePerUnit();

        logger.info("%s Received product addition validation failed event, performing compensation actions for productId %s and basketId %s"
                .formatted(LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX, productId, basketId));

        basketService.removeProductEntryWithinQuantity(basketId, productId, quantity, pricePerUnit);
    }

    private void handleOrderPlacementRequestedEvent(DomainEventEnvelope<OrderPlacementRequestedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = event.getBasketId();
        Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

        logger.info("{} Received {}, checking out basket {} for order {}",
                LoggingUtils.PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, orderId);

        basketService.checkOutBasket(basketId, orderId);
    }

    private void handleOrderPlacedEvent(DomainEventEnvelope<OrderPlacedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = event.getBasketId();
        Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

        logger.info("{} Received {} event, checking in basket {} for order {}",
                LoggingUtils.PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, orderId);

        basketService.checkInBasket(basketId, orderId);
    }
}
