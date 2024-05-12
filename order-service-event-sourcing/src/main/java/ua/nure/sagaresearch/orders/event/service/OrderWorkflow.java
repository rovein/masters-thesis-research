package ua.nure.sagaresearch.orders.event.service;

import io.eventuate.EventHandlerContext;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketCheckedOutEvent;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;
import ua.nure.sagaresearch.orders.event.domain.FillOrderWithProductEntriesCommand;
import ua.nure.sagaresearch.orders.event.domain.Order;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@EventSubscriber(id = "orderWorkflow")
public class OrderWorkflow {

    @EventHandlerMethod
    public CompletableFuture<?> handleBasketCheckedOut(
            EventHandlerContext<SourcingBasketCheckedOutEvent> ctx) {
        SourcingBasketCheckedOutEvent event = ctx.getEvent();
        Money totalPrice = event.getTotalPrice();
        Map<String, ProductBasketEntry> productEntries = event.getProductEntries();
        String basketId = ctx.getEntityId();
        String orderId = event.getOrderId();

        return ctx.update(Order.class, orderId, new FillOrderWithProductEntriesCommand(basketId, totalPrice, productEntries));
    }
}
