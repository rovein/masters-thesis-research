package ua.nure.sagaresearch.orders.event.service;

import io.eventuate.EventHandlerContext;
import io.eventuate.EventHandlerMethod;
import io.eventuate.EventSubscriber;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductQuantityReservedEvent;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductQuantityRestoredEvent;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketCheckedOutEvent;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;
import ua.nure.sagaresearch.orders.event.domain.ApplyProductQuantityReservationCommand;
import ua.nure.sagaresearch.orders.event.domain.ApplyProductQuantityRestorationCommand;
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

    @EventHandlerMethod
    public CompletableFuture<?> handleProductQuantityReserved(
            EventHandlerContext<SourcingProductQuantityReservedEvent> ctx) {
        SourcingProductQuantityReservedEvent event = ctx.getEvent();
        String orderId = event.getOrderId();
        String productId = ctx.getEntityId();

        return ctx.update(Order.class, orderId, new ApplyProductQuantityReservationCommand(orderId, productId));
    }

    @EventHandlerMethod
    public CompletableFuture<?> handleProductQuantityRestored(
            EventHandlerContext<SourcingProductQuantityRestoredEvent> ctx) {
        SourcingProductQuantityRestoredEvent event = ctx.getEvent();
        String orderId = event.getOrderId();
        String productId = ctx.getEntityId();

        return ctx.update(Order.class, orderId, new ApplyProductQuantityRestorationCommand(orderId, productId));
    }
}
