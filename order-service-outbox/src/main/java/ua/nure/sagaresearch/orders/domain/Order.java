package ua.nure.sagaresearch.orders.domain;

import static java.util.Collections.singletonList;

import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;
import ua.nure.sagaresearch.orders.domain.events.OrderPlacementRequestedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderState;
import ua.nure.sagaresearch.common.domain.product.ProductOrderEntry;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Embedded
    private OrderDetails orderDetails;

    @Embedded
    private Money totalPrice;

    @ElementCollection
    private Map<String, ProductOrderEntry> productEntries;

    private LocalDate creationDate;

    @Version
    private Long version;

    public Order(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
        this.state = OrderState.PENDING;
        this.totalPrice = Money.ZERO;
        this.creationDate = LocalDate.now();
        this.productEntries = Collections.emptyMap();
    }

    public static ResultWithEvents<Order> createOrder(OrderDetails orderDetails) {
        Order order = new Order(orderDetails);
        OrderPlacementRequestedEvent orderPlacementRequestedEvent = new OrderPlacementRequestedEvent(Long.valueOf(orderDetails.getBasketId()));
        return new ResultWithEvents<>(order, singletonList(orderPlacementRequestedEvent));
    }

    public void onPaymentSuccess() {
        this.state = OrderState.APPROVED;
    }

    public void onPaymentFailed() {
        this.state = OrderState.REJECTED;
    }

    public void cancel() {
        switch (state) {
            case PENDING:
                throw new PendingOrderCantBeCancelledException();
            case CANCELLATION_REQUESTED:
                this.state = OrderState.CANCELLED;
                return;
            default:
                throw new UnsupportedOperationException("Can't cancel in this state: " + state);
        }
    }
}
