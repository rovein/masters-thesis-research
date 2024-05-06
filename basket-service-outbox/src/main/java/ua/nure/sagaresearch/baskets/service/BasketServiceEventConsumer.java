package ua.nure.sagaresearch.baskets.service;

import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidationFailedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketPriceHasChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
                    .onEvent(ProductBasketPriceHasChangedEvent.class, this::handleProductBasketPriceHasChangedEvent)
                    .onEvent(ProductBasketAdditionValidationFailedEvent.class, this::handleProductBasketAdditionValidationFailedEvent)
                .andForAggregateType("ua.nure.sagaresearch.orders.domain.Order")
                    .onEvent(OrderPlacementRequestedEvent.class, this::handleOrderPlacementRequestedEvent)
                    .onEvent(OrderPlacedEvent.class, this::handleOrderPlacedEvent)
                .build();
    }

    public void handleProductBasketAdditionValidatedEvent(DomainEventEnvelope<ProductBasketAdditionValidatedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        log(logger, "{} Received {}, transaction completed successfully for productId {} and basketId {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, event.getClass().getSimpleName(), domainEventEnvelope.getAggregateId(), event.getBasketId());
    }

    private void handleProductBasketPriceHasChangedEvent(DomainEventEnvelope<ProductBasketPriceHasChangedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        String productId = domainEventEnvelope.getAggregateId();
        Long basketId = event.getBasketId();
        Money actualPricePerUnit = event.getActualPricePerUnit();

        log(logger, "{} Received {}, updating price of product {} in basket {} to {}$",
                ADD_PRODUCT_TO_BASKET_PREFIX, event.getClass().getSimpleName(), productId, basketId, actualPricePerUnit.getAmount());

        basketService.actualizeProductEntryPrice(basketId, productId, actualPricePerUnit);
    }

    public void handleProductBasketAdditionValidationFailedEvent(DomainEventEnvelope<ProductBasketAdditionValidationFailedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        String productId = domainEventEnvelope.getAggregateId();
        Long basketId = event.getBasketId();

        log(logger, "{} Received {}, performing compensation actions for productId {} and basketId {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, event.getClass().getSimpleName(), productId, basketId);

        basketService.removeProductEntry(basketId, productId);
    }

    private void handleOrderPlacementRequestedEvent(DomainEventEnvelope<OrderPlacementRequestedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = event.getBasketId();
        Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

        log(logger, "{} Received {}, checking out basket {} for order {}",
                PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, orderId);

        basketService.checkOutBasket(basketId, orderId);
    }

    private void handleOrderPlacedEvent(DomainEventEnvelope<OrderPlacedEvent> domainEventEnvelope) {
        var event = domainEventEnvelope.getEvent();
        Long basketId = event.getBasketId();
        Long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());

        log(logger, "{} Received {} event, checking in basket {} for order {}",
                PLACE_ORDER_PREFIX, event.getClass().getSimpleName(), basketId, orderId);

        basketService.checkInBasket(basketId, orderId);
    }
}
