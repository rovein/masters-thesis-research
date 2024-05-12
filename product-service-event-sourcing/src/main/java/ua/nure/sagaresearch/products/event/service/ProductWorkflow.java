package ua.nure.sagaresearch.products.event.service;

import io.eventuate.EventHandlerContext;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingProductAddedToBasketEvent;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.products.event.domain.Product;
import ua.nure.sagaresearch.products.event.domain.ValidateProductAddedToBasketCommand;

import java.util.concurrent.CompletableFuture;

@EventSubscriber(id = "productWorkflow")
public class ProductWorkflow {

    @EventHandlerMethod
    public CompletableFuture<?> validateProduct(
            EventHandlerContext<SourcingProductAddedToBasketEvent> ctx) {
        SourcingProductAddedToBasketEvent event = ctx.getEvent();
        String productId = event.getProductId();
        String basketId = ctx.getEntityId();
        Money pricePerUnit = event.getPricePerUnit();

        return ctx.update(Product.class, productId, new ValidateProductAddedToBasketCommand(basketId, pricePerUnit));
    }

}