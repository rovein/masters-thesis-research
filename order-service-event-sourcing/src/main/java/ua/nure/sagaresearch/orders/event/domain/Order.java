package ua.nure.sagaresearch.orders.event.domain;

import static ua.nure.sagaresearch.common.util.ConverterUtil.convertToProductOrderEntries;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logAggregateProcessMethod;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.product.ProductOrderEntry;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;
import ua.nure.sagaresearch.orders.domain.events.OrderState;
import ua.nure.sagaresearch.orders.domain.events.sourcing.SourcingOrderPlacedEvent;
import ua.nure.sagaresearch.orders.domain.events.sourcing.SourcingOrderPlacementRequestedEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Order extends ReflectiveMutableCommandProcessingAggregate<Order, OrderCommand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);

    private OrderState state;

    private OrderDetails orderDetails;

    private Money totalPrice;

    private Map<String, ProductOrderEntry> productEntries;

    private LocalDate creationDate;

    public List<Event> process(CreateOrderCommand cmd) {
        SourcingOrderPlacementRequestedEvent event = new SourcingOrderPlacementRequestedEvent(
                cmd.getBasketId(),
                cmd.getShippingType(),
                cmd.getPaymentType(),
                cmd.getShippingAddress());
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_PLACE_ORDER_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingOrderPlacementRequestedEvent event) {
        this.state = OrderState.PENDING;
        this.orderDetails = new OrderDetails(event.getBasketId(), event.getShippingType(), event.getPaymentType(), event.getShippingAddress());
        this.totalPrice = Money.ZERO;
        this.creationDate = LocalDate.now();
        this.productEntries = new HashMap<>();
    }

    public List<Event> process(FillOrderWithProductEntriesCommand cmd) {
        SourcingOrderPlacedEvent event = new SourcingOrderPlacedEvent(
                cmd.getBasketId(),
                cmd.getTotalPrice(),
                cmd.getProductEntries()
        );
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_PLACE_ORDER_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingOrderPlacedEvent event) {
        this.productEntries = convertToProductOrderEntries(event.getProductEntries());
        this.totalPrice = event.getTotalPrice();
    }
}
