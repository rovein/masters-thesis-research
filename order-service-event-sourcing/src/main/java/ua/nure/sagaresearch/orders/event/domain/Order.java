package ua.nure.sagaresearch.orders.event.domain;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;
import ua.nure.sagaresearch.orders.domain.events.OrderState;
import ua.nure.sagaresearch.orders.domain.events.ProductOrderEntry;
import ua.nure.sagaresearch.orders.domain.events.sourcing.SourcingOrderPlacementRequestedEvent;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Order extends ReflectiveMutableCommandProcessingAggregate<Order, OrderCommand> {

    private OrderState state;

    private OrderDetails orderDetails;

    private Money totalPrice;

    private Map<Long, ProductOrderEntry> productEntries;

    private LocalDate creationDate;

    public List<Event> process(CreateOrderCommand cmd) {
        return EventUtil.events(new SourcingOrderPlacementRequestedEvent(
                cmd.getBasketId(),
                cmd.getShippingType(),
                cmd.getPaymentType(),
                cmd.getShippingAddress()));
    }

    public void apply(SourcingOrderPlacementRequestedEvent event) {
        this.state = OrderState.PENDING;
        this.orderDetails = new OrderDetails(event.getBasketId(), event.getShippingType(), event.getPaymentType(), event.getShippingAddress());
        this.totalPrice = Money.ZERO;
        this.creationDate = LocalDate.now();
        this.productEntries = Collections.emptyMap();
    }

}
