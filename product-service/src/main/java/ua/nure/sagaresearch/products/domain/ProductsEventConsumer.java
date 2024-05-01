package ua.nure.sagaresearch.products.domain;

import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;

import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.sagaresearch.baskets.domain.events.ProductAddedToBasketEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderPaymentConfirmedEvent;
import ua.nure.sagaresearch.products.service.ProductService;

public class ProductsEventConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ProductService productService;

    public ProductsEventConsumer(ProductService productService) {
        this.productService = productService;
    }

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("ua.nure.sagaresearch.baskets.domain.Basket")
                    .onEvent(ProductAddedToBasketEvent.class, this::productAddedToBasketEventHandler)
                .andForAggregateType("ua.nure.sagaresearch.orders.domain.Order")
                    .onEvent(OrderPaymentConfirmedEvent.class, this::handleOrderPaymentConfirmedEvent)
                .build();
    }

    private void productAddedToBasketEventHandler(DomainEventEnvelope<ProductAddedToBasketEvent> domainEventEnvelope) {
        ProductAddedToBasketEvent event = domainEventEnvelope.getEvent();
        long basketId = Long.parseLong(domainEventEnvelope.getAggregateId());
        log(logger, "{} Handling {} for basket {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, event.getClass().getSimpleName(), basketId);

        productService.validateProductAddedToBasket(
                event.getProductId(),
                basketId,
                event.getPricePerUnit()
        );
    }

    private void handleOrderPaymentConfirmedEvent(DomainEventEnvelope<OrderPaymentConfirmedEvent> domainEventEnvelope) {
        OrderPaymentConfirmedEvent event = domainEventEnvelope.getEvent();
        long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());
        log(logger, "{} Handling {} for order {}",
                CONFIRM_PAYMENT_PREFIX, event.getClass().getSimpleName(), orderId);

        productService.updateProductsQuantityForOrder(orderId, event.getProductEntries());
    }
}
