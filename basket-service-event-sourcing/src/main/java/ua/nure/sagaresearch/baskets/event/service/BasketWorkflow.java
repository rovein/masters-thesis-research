package ua.nure.sagaresearch.baskets.event.service;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EventHandlerContext;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketPriceHasChangedEvent;
import ua.nure.sagaresearch.baskets.event.domain.Basket;
import ua.nure.sagaresearch.baskets.event.domain.HandleProductValidationCommand;
import ua.nure.sagaresearch.baskets.event.domain.UpdateBasketProductPriceCommand;
import ua.nure.sagaresearch.common.domain.Money;

import java.util.concurrent.CompletableFuture;

@EventSubscriber(id = "basketWorkflow")
public class BasketWorkflow {

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handleSuccessValidation(
            EventHandlerContext<SourcingProductBasketAdditionValidatedEvent> ctx) {
        SourcingProductBasketAdditionValidatedEvent event = ctx.getEvent();
        String productId = ctx.getEntityId();
        String basketId = event.getBasketId();

        return ctx.update(Basket.class, basketId, new HandleProductValidationCommand(productId));
    }

    @EventHandlerMethod
    public CompletableFuture<EntityWithIdAndVersion<Basket>> handlePriceHasChanged(
            EventHandlerContext<SourcingProductBasketPriceHasChangedEvent> ctx) {
        SourcingProductBasketPriceHasChangedEvent event = ctx.getEvent();
        String productId = ctx.getEntityId();
        String basketId = event.getBasketId();
        Money actualPricePerUnit = event.getActualPricePerUnit();

        return ctx.update(Basket.class, basketId, new UpdateBasketProductPriceCommand(productId, actualPricePerUnit));
    }
}
