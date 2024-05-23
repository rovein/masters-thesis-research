package ua.nure.sagaresearch.baskets.event.service;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EventHandlerContext;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketPriceHasChangedEvent;
import ua.nure.sagaresearch.baskets.event.domain.Basket;
import ua.nure.sagaresearch.baskets.event.domain.CheckoutBasketCommand;
import ua.nure.sagaresearch.baskets.event.domain.ClearBasketCommand;
import ua.nure.sagaresearch.baskets.event.domain.HandleProductValidationCommand;
import ua.nure.sagaresearch.baskets.event.domain.UpdateBasketProductPriceCommand;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.sourcing.SourcingOrderPlacedEvent;
import ua.nure.sagaresearch.orders.domain.events.sourcing.SourcingOrderPlacementRequestedEvent;

import java.util.concurrent.CompletableFuture;

@EventSubscriber(id = "basketWorkflow")
public class BasketWorkflow {

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handleSuccessValidation(
            EventHandlerContext<SourcingProductBasketAdditionValidatedEvent> ctx) {
        SourcingProductBasketAdditionValidatedEvent event = ctx.getEvent();
        String productId = ctx.getEntityId();
        String basketId = event.getBasketId();

        return ctx.update(Basket.class, basketId, new HandleProductValidationCommand(basketId, productId));
    }

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handlePriceHasChanged(
            EventHandlerContext<SourcingProductBasketPriceHasChangedEvent> ctx) {
        SourcingProductBasketPriceHasChangedEvent event = ctx.getEvent();
        String productId = ctx.getEntityId();
        String basketId = event.getBasketId();
        Money actualPricePerUnit = event.getActualPricePerUnit();

        return ctx.update(Basket.class, basketId, new UpdateBasketProductPriceCommand(basketId, productId, actualPricePerUnit));
    }

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handleOrderPlacementRequested(
            EventHandlerContext<SourcingOrderPlacementRequestedEvent> ctx) {
        SourcingOrderPlacementRequestedEvent event = ctx.getEvent();
        String basketId = event.getBasketId();
        String orderId = ctx.getEntityId();

        return ctx.update(Basket.class, basketId, new CheckoutBasketCommand(orderId));
    }

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handleOrderPlacement(
            EventHandlerContext<SourcingOrderPlacedEvent> ctx) {
        SourcingOrderPlacedEvent event = ctx.getEvent();
        String basketId = event.getBasketId();
        String orderId = ctx.getEntityId();

        return ctx.update(Basket.class, basketId, new ClearBasketCommand(orderId));
    }


}
